package techcourse.myblog.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.CommentNotFoundException;
import techcourse.myblog.domain.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(long id, long loginUserId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("댓글을 찾지 못했습니다."));
        comment.isAuthor(loginUserId);
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment update(long commentId, User user, String contents) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글을 찾지 못했습니다."));
        comment.update(contents, user.getId());
        return comment;
    }
}
