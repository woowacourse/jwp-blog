package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentDto;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.UserDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final ArticleService articleService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, ArticleService articleService, UserService userService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
    }

    public void create(long articleId, CommentDto commentDto, UserDto userDto) {
        ArticleDto articleDto = articleService.readById(articleId);
        commentRepository.save(commentDto.toEntity(userDto, articleDto));
    }

    @Transactional
    public void update(long commentId, CommentDto commentDto, UserDto userDto) {
        checkAuthor(commentId, userDto);
        Comment comment = commentRepository.findById(commentId).get();
        comment.update(commentDto.toEntity());
    }

    public void delete(long commentId, UserDto userDto) {
        checkAuthor(commentId, userDto);
        commentRepository.deleteById(commentId);
    }

    private void checkAuthor(long commentId, UserDto userDto) {
        UserDto findUserDto = userService.findByUserEmail(userDto);
        commentRepository.findById(commentId).ifPresent(comment -> {
            if (findUserDto.toEntity().checkAuthor(commentId)) {
                throw new IllegalArgumentException("허가되지 않은 사용자입니다.");
            }
        });
    }

    public List<CommentDto> findByArticleId(long articleId) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }

    public void deleteByArticleId(long articleId) {
        commentRepository.deleteByArticleId(articleId);
    }
}
