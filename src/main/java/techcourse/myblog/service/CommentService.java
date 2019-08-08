package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
public class CommentService extends AbstractDomainService<Comment> {

    private static final String NOT_FOUND_COMMENT_ERROR = "존재하지 않는 댓글";

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void delete(Long id, User user) {
        Comment comment = getAuthorizedDomain(id, user);
        commentRepository.delete(comment);
    }

    @Override
    protected Comment getAuthorizedDomain(Long commentId, User user) {
        Comment comment = findCommentById(commentId);
        checkAuthorizedUser(user, comment);
        return comment;
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(commentId));
                    return new NotFoundCommentException(NOT_FOUND_COMMENT_ERROR);
                });
    }

    public Comment update(Long commentId, CommentRequest commentRequest, User user) {
        Comment comment = getAuthorizedDomain(commentId, user);
        return comment.update(commentRequest.getContents());
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public List<Comment> findCommentsByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }
}
