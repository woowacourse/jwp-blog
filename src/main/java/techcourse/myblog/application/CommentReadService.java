package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.article.exception.MismatchAuthorException;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentReadService {
    private final CommentRepository commentRepository;

    public CommentReadService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findByArticleId(Long articleId) {
        return Collections.unmodifiableList(commentRepository.findByArticleId(articleId));
    }

    public Comment findByIdAndWriter(Long commentId, User user) {
        return commentRepository.findByIdAndWriter(commentId, user)
                .orElseThrow(MismatchAuthorException::new);
    }
}
