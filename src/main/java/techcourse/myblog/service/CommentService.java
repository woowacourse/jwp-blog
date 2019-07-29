package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public boolean tryUpdate(long commentId, Comment toUpdate) {
        return commentRepository.findById(commentId).map(ifExists -> {
                                                            toUpdate.setId(commentId);
                                                            commentRepository.save(toUpdate);
                                                            return true;
                                                        }).orElse(false);
    }

    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }
}