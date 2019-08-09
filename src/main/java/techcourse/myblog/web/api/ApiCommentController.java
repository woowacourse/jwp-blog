package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.CommentResponseDto;
import techcourse.myblog.service.CommentGenericService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class ApiCommentController {
    private static final Logger log = LoggerFactory.getLogger(ApiCommentController.class);

    private final CommentGenericService commentService;

    public ApiCommentController(CommentGenericService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<List<CommentResponseDto>> create(@PathVariable long articleId,
                                                           @RequestBody CommentDto commentDto,
                                                           @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Create] API: ArticleId = {} Comment = {}", articleId, commentDto.getContents());
        commentService.add(articleId, userSessionInfo.toUser(), commentDto, CommentResponseDto.class);
        List<CommentResponseDto> comments = commentService.findByArticle(articleId, CommentResponseDto.class);
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable long commentId,
                                                     @RequestBody CommentDto commentDto,
                                                     @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Update] API: ArticleId = {} Comment = {}", commentId, commentDto.getContents());
        CommentResponseDto comment = commentService.update(commentId, userSessionInfo.toUser(), commentDto, CommentResponseDto.class);
        return new ResponseEntity<>(comment, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<List<CommentResponseDto>> delete(@PathVariable long articleId,
                                                           @PathVariable long commentId,
                                                           @SessionInfo UserSessionInfo userSessionInfo) {
        log.debug("[Comment] [Delete] API: CommentId = {}", commentId);
        commentService.delete(commentId, userSessionInfo.toUser());
        List<CommentResponseDto> comments = commentService.findByArticle(articleId, CommentResponseDto.class);
        return new ResponseEntity<>(comments, HttpStatus.ACCEPTED);
    }
}
