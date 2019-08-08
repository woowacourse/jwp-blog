package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.exception.MismatchCommentWriterException;
import techcourse.myblog.service.exception.NotFoundCommentException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByArticleId(Long articleId) {
        return Collections.unmodifiableList(commentRepository.findByArticleId(articleId));
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Long deleteById(Long id, User user) {
        if (findById(id).matchWriter(user)) {
            commentRepository.deleteById(id);
            return id;
        }
        throw new MismatchCommentWriterException();
    }

    public Comment modify(Long id, CommentDto commentDto, User user) {
        Comment comment = findById(id);
        Article article = comment.getArticle();
        return comment.update(commentDto.toComment(user, article));
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
    }

    public long countByArticleId(Long articleId) {
        return commentRepository.countByArticleId(articleId);
    }
}
