package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(final ArticleRepository articleRepository, final CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    public Comment create(final Long articleId, final User user, final Comment comment) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("해당 게시물이 없습니다."));

        comment.initialize(user, article);
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public void update(final String content, final Long commentId, final User user) {
        Comment comment = findById(commentId, user);
        comment.update(content);
    }

    public void delete(final Long commentId, final User user) {
        Comment comment = findById(commentId, user);
        commentRepository.delete(comment);
    }

    private Comment findById(final Long commentId, final User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("해당 댓글을 찾을 수 없습니다"));
        if (!comment.getUser().equals(user)) {
            throw new NotMatchAuthenticationException("권한이 없습니다.");
        }
        return comment;
    }
}
