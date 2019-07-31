package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(User user, Article article, CommentDto commentDto) {
        Comment comment = new Comment(user, article, commentDto.getContents());
        commentRepository.save(comment);
        comment.getArticle().addComment(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
    }

    @Transactional
    public Comment update(CommentDto commentDto, Long commentId) {
        Comment oldComment = findById(commentId);
        Comment updatedComment = new Comment(oldComment.getAuthor(), oldComment.getArticle(), commentDto.getContents());
        return oldComment.update(updatedComment);
    }

    public boolean isOwnerOf(Long commentId, User user) {
        Comment comment = findById(commentId);
        return comment.getAuthor().getId().equals(user.getId());
    }

    public void delete(Long commentId) {
        Comment comment = findById(commentId);
        comment.getArticle().deleteComment(comment);
        commentRepository.deleteById(commentId);
    }
}
