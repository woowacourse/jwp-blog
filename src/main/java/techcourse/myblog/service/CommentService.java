package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.presentation.CommentNotFoundException;
import techcourse.myblog.service.dto.CommentRequestDto;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment update(long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.update(commentRequestDto);
        return comment;
    }

    @Transactional
    public void deleteByArticle(Article article) {
        commentRepository.findByArticleOrderByCreatedAt(article)
                .stream()
                .forEach(comment -> commentRepository.deleteById(comment.getId()));
    }
}
