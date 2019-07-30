package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findById(final Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Comment> findByArticle(final Article article) {
        return commentRepository.findByArticle(article);
    }

    public Comment save(final Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment update(final Long commentId, final CommentDto dto) {
        final Comment selectedComment = findById(commentId);
        return commentRepository.save(selectedComment.update(dto));
    }

    public Long delete(final Long commentId) {
        final Long articleId = findById(commentId).getArticle().getId();
        commentRepository.deleteById(commentId);
        return articleId;
    }
}
