package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;

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

    public Comment findByIdAndWriter(Long commentId, User user) {
        return commentRepository.findByIdAndWriter(commentId, user)
                .orElseThrow(MismatchAuthorException::new);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteById(Long id, User user) {
        checkDeleteAuth(id, user);
        commentRepository.deleteById(id);
    }

    private void checkDeleteAuth(Long id, User user) {
        findByIdAndWriter(id, user);
    }

    public void modify(Long commentId, Comment comment) {
        commentRepository.findById(commentId)
                .ifPresent(existComment -> existComment.update(comment));
    }
}
