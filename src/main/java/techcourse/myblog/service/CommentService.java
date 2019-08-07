package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.presentation.CommentNotFoundException;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public CommentService(CommentRepository commentRepository, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
    }

    @Transactional
    public Comment update(long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.update(commentRequestDto.toComment());
        return comment;
    }

    public Comment save(Comment newComment) {
        return commentRepository.save(newComment);
    }

    public List<Comment> findByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticleOrderByCreatedAt(article));
    }

    public List<CommentResponseDto> commentsToDtos(List<Comment> comments) {
        return Collections.unmodifiableList(comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()));
    }

    public Comment findById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    public void deleteById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentResponseDto> findByArticleId(long articleId) {
        return findByArticle(articleService.findById(articleId))
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
