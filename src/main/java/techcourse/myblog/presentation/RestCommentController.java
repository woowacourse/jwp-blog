package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.web.LoggedInUser;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class RestCommentController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public RestCommentController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable long articleId) {
        return new ResponseEntity<>(commentService.findByArticleId(articleId), HttpStatus.OK);
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> addNewComment(@PathVariable long articleId,
                                                                  @RequestBody CommentRequestDto commentRequestDto,
                                                                  @LoggedInUser User user) {
        Article article = articleService.findById(articleId);
        commentService.save(commentRequestDto.toComment(user, article));
        return new ResponseEntity<>(commentService.findByArticleId(articleId), HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> updateComment(@PathVariable long commentId,
                                                                  @RequestBody CommentRequestDto commentRequestDto,
                                                                  @LoggedInUser User user) {
        Comment comment = commentService.findById(commentId);
        if (!comment.writtenBy(user)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        commentService.update(commentId, commentRequestDto);
        return new ResponseEntity<>(commentService.findByArticleId(comment.getArticle().getId()), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> deleteComment(@PathVariable long commentId,
                                                                  @LoggedInUser User user) {
        Comment comment = commentService.findById(commentId);
        if (!comment.writtenBy(user)) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        commentService.deleteById(commentId);
        return new ResponseEntity<>(commentService.findByArticleId(comment.getArticle().getId()), HttpStatus.OK);
    }
}
