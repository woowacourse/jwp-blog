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

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteById(Long id, User user) {
        if (findById(id).matchWriter(user)) {
            commentRepository.deleteById(id);
        }
        throw new MismatchAuthorException();
    }

    public void modify(Long id, Comment comment) {
        findById(id).update(comment);
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(NotFoundCommentException::new);
    }
}
