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
public class CommentApi {
    private static final Logger log = LoggerFactory.getLogger(CommentApi.class);

    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentApi(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<List<Comment>> create(@PathVariable long articleId,
                                                @RequestBody CommentDto.JSON json,
                                                @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Create] API: ArticleId = {} Comment = {}", articleId, json.getContents());
        commentService.addComment(articleId, userSessionInfo.toUser(), json.toDto());
        List<Comment> comments = articleService.findArticle(articleId).getComments();
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<List<Comment>> update(@PathVariable long articleId,
                                                @PathVariable long commentId,
                                                @RequestBody CommentDto.JSON json,
                                                @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Update] API: ArticleId = {} Comment = {}", commentId, json.getContents());
        commentService.updateComment(commentId, userSessionInfo.toUser(), json.toDto());
        List<Comment> comments = articleService.findArticle(articleId).getComments();
        return new ResponseEntity<>(comments, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<List<Comment>> delete(@PathVariable long articleId,
                                                @PathVariable long commentId,
                                                @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Delete] API: CommentId = {}", commentId);
        commentService.deleteComment(commentId, userSessionInfo.toUser());
        List<Comment> comments = articleService.findArticle(articleId).getComments();
        return new ResponseEntity<>(comments, HttpStatus.ACCEPTED);
    }
}
