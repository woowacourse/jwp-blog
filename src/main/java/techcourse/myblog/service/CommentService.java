package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.service.exception.NotFoundCommentException;
import techcourse.myblog.service.util.DayCalculator;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
    }

    public List<CommentResponseDto> findCommentsByArticleId(long articleId) {
        Article article = articleService.findById(articleId);
        return commentRepository.findAllByArticle(article)
                .stream()
                .map(this::toCommentResponseDto)
                .collect(Collectors.toList());
    }

    public Comment save(UserSessionDto userSessionDto, CommentRequestDto commentRequestDto) {
        User user = userService.findByUserSession(userSessionDto);
        Article article = articleService.findById(commentRequestDto.getArticleId());
        Comment comment = commentRequestDto.toEntity(user, article);
        return commentRepository.save(comment);
    }

    public Comment update(UserSessionDto userSessionDto, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = findById(commentId);
        User author = userService.findByUserSession(userSessionDto);
        comment.updateComment(commentRequestDto.toEntity(author));
        return comment;
    }

    public void delete(UserSessionDto userSessionDto, Long commentId) {
        Comment comment = findById(commentId);
        User author = userService.findByUserSession(userSessionDto);
        if (comment.matchAuthor(author)) {
            commentRepository.deleteById(commentId);
        }
    }

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        int day = comment.subtractionOfDays(new DayCalculator());
        return new CommentResponseDto(comment.getId(), comment.getAuthorId(),
                comment.getAuthorName(), comment.getComment(), day);
    }
}
