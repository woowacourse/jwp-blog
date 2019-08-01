package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.translator.ModelTranslator;
import techcourse.myblog.translator.UserTranslator;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final ModelTranslator<User, UserDto> userTranslator;

    public CommentService(final ArticleRepository articleRepository, final CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userTranslator = new UserTranslator();
    }


    public Comment create(final Long articleId, final UserDto userDto, final Comment comment) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("해당 게시물이 없습니다."));

        comment.initialize(userTranslator.toEntity(new User(), userDto), article);
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public void update(final String content, final Long commentId, final UserDto userDto) {
        Comment comment = findById(commentId, userTranslator.toEntity(new User(), userDto));
        comment.update(content);
    }

    public void delete(final Long commentId, final UserDto userDto) {
        Comment comment = findById(commentId, userTranslator.toEntity(new User(), userDto));
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Comment findById(final Long commentId, final User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다"));
        if (!comment.getUser().equals(user)) {
            throw new NotMatchAuthenticationException("권한이 없습니다.");
        }
        return comment;
    }
}
