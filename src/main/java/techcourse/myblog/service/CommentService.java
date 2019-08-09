package techcourse.myblog.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Transactional
    public void addComment(CommentRequestDto commentRequestDto, long articleId, User user) {
        commentRepository.save(commentRequestDto.toComment(user, articleService.findById(articleId)));
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

    public void deleteById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentResponseDto> findAllByArticleId(long articleId) {
        return findByArticle(articleService.findById(articleId))
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public void validateCommenter(long commentId, User commenter) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (!comment.writtenBy(commenter)) {
            throw new CommenterMismatchException();
        }
    }

    public ResponseEntity<List<CommentResponseDto>> makeResponseWithCommentsOf(long articleId) {
        return new ResponseEntity<>(findAllByArticleId(articleId), HttpStatus.OK);
    }

    public long getArticleIdOf(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        return comment.getArticle().getId();
    }

    public ResponseEntity<List<CommentResponseDto>> updateAndMakeResponseWithCommentsOf(long commentId, CommentRequestDto commentRequestDto, User user) {
        long articleId = getArticleIdOf(commentId);
        validateCommenter(commentId, user);
        update(commentId, commentRequestDto);
        return makeResponseWithCommentsOf(articleId);
    }

    public ResponseEntity<List<CommentResponseDto>> deleteAndMakeResponseWithCommentsOf(long commentId, User user) {
        long articleId = getArticleIdOf(commentId);
        validateCommenter(commentId, user);
        deleteById(commentId);
        return makeResponseWithCommentsOf(articleId);
    }
}
