package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.web.UserSession;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RestController
@RequestMapping(value = "/api/articles/{articleId}/comments")
public class CommentRestController {
    private static final Logger log = LoggerFactory.getLogger(CommentRestController.class);

    private final CommentService commentService;

    public CommentRestController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto.Response>> list(@PathVariable Long articleId) {
        log.debug("articleId:{}", articleId);

        final List<CommentDto.Response> comments = commentService.findAllByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentDto.Response> create(@PathVariable Long articleId,
                                                      @RequestBody CommentDto.Create commentCreate,
                                                      UserSession userSession) {
        log.debug("contents: {}", commentCreate.getContents());

        commentCreate.setArticleId(articleId);
        final CommentDto.Response savedComment = commentService.save(commentCreate, userSession.getId());
        final URI uri = linkTo(CommentRestController.class, articleId).toUri();
        return ResponseEntity.created(uri).body(savedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity delete(@PathVariable Long articleId,
                                 @PathVariable Long commentId,
                                 UserSession userSession) {
        log.debug("commentId: {}", commentId);

        commentService.delete(commentId, userSession.getId());

        final URI uri = linkTo(CommentRestController.class, articleId).toUri();
        return ResponseEntity.noContent().location(uri).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity update(@PathVariable Long articleId,
                                 @PathVariable Long commentId,
                                 @RequestBody CommentDto.Update commentUpdate,
                                 UserSession userSession) {
        log.debug("commentId: {}", commentId);

        commentUpdate.setArticleId(articleId);
        commentUpdate.setId(commentId);
        final CommentDto.Response updatedComment = commentService.update(commentUpdate, userSession.getId());

        return ResponseEntity.ok().body(updatedComment);
    }
}
