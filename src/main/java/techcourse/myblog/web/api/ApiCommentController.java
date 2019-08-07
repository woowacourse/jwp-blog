package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class ApiCommentController {
    private static final Logger log = LoggerFactory.getLogger(ApiCommentController.class);

    private final CommentService commentService;
    private final ArticleService articleService;

    public ApiCommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<List<Comment>> create(@PathVariable long articleId,
                                                @RequestBody CommentDto commentDto,
                                                @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Create] API: ArticleId = {} Comment = {}", articleId, commentDto.getContents());
        commentService.add(articleId, userSessionInfo.toUser(), commentDto);
        List<Comment> comments = articleService.findArticle(articleId).getComments();
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> update(@PathVariable long articleId,
                                          @PathVariable long commentId,
                                          @RequestBody CommentDto commentDto,
                                          @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Update] API: ArticleId = {} Comment = {}", commentId, commentDto.getContents());
        Comment comment = commentService.update(commentId, userSessionInfo.toUser(), commentDto);
        return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<List<Comment>> delete(@PathVariable long articleId,
                                                @PathVariable long commentId,
                                                @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Delete] API: CommentId = {}", commentId);
        commentService.delete(commentId, userSessionInfo.toUser());
        List<Comment> comments = articleService.findArticle(articleId).getComments();
        return new ResponseEntity<>(comments, HttpStatus.ACCEPTED);
    }
}
