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

    public void save(CommentDto commentDto) {
        Article article = commentDto.getArticle();
        Comment comment = new Comment(commentDto.getContents(), commentDto.getUser(), article);
        article.addComment(comment);

        commentRepository.save(comment);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
    }

    @Transactional
    public Comment update(CommentDto commentDto, Long commentId) {
        Comment oldComment = findById(commentId);
        Comment updatedComment = new Comment(
                commentDto.getContents(), oldComment.getAuthor(), oldComment.getArticle());
        return oldComment.update(updatedComment);
    }

    public void delete(Long commentId) {
        Comment comment = findById(commentId);
        Article parentArticle = comment.getArticle();

        parentArticle.deleteComment(comment);
        commentRepository.deleteById(commentId);
    }

    public void checkOwner(Long commentId, User user) {
        Comment comment = findById(commentId);
        comment.checkOwner(user);
    }
}
