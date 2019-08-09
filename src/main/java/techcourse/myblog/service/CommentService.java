package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.presentation.CommentNotFoundException;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.exception.CommenterMismatchException;

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

    public void addComment(CommentRequestDto commentRequestDto, long articleId, User user) {
        commentRepository.save(commentRequestDto.toComment(user, articleService.findById(articleId)));
    }

    @Transactional
    public long update(long commentId, CommentRequestDto commentRequestDto, User user) {
        validateCommenter(commentId, user);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.update(commentRequestDto.toComment());
        return comment.getArticle().getId();
    }

    public void save(Comment newComment) {
        commentRepository.save(newComment);
    }

    public long delete(long commentId, User user) {
        long articleId = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new).getArticle().getId();
        validateCommenter(commentId, user);
        commentRepository.deleteById(commentId);
        return articleId;
    }

    public List<CommentResponseDto> findAllByArticleId(long articleId) {
        return findAllByArticle(articleService.findById(articleId))
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<Comment> findAllByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticleOrderByCreatedAt(article));
    }

    public List<CommentResponseDto> commentsToDtos(List<Comment> comments) {
        return Collections.unmodifiableList(comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()));
    }

    private void validateCommenter(long commentId, User commenter) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!comment.writtenBy(commenter)) {
            throw new CommenterMismatchException();
        }
    }
}
