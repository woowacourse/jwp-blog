package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;

@Service
@Transactional
public class CommentWriteService {
    private final CommentRepository commentRepository;
    private final CommentReadService commentReadService;

    public CommentWriteService(CommentRepository commentRepository, CommentReadService commentReadService) {
        this.commentRepository = commentRepository;
        this.commentReadService = commentReadService;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment modify(Long commentId, Comment comment) {
        return commentReadService.findById(commentId).update(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
