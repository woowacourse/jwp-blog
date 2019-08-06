package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.CommentRepository;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.user.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(CommentDto commentDto, User author) {
        Comment comment = commentDto.toEntity(author);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
        comment.checkAvailableUserForDelete(user);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Comment updateComment(Long commentId, User user, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
        Comment updateComment = commentDto.toEntity(user);
        comment.update(updateComment, user);
        return comment;
    }
}
