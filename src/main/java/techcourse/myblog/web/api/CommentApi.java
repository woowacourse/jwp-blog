package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.UserSession;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentApi {
    private static final Logger log = LoggerFactory.getLogger(CommentApi.class);

    private final CommentService commentService;

    public CommentApi(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<List<CommentDto.Response>> create(@PathVariable Long articleId,
                                                            @RequestBody CommentDto.Create commentCreate,
                                                            UserSession userSession) {
        log.debug("contents: {}", commentCreate.getContents());

        commentCreate.setArticleId(articleId);
        commentService.save(commentCreate, userSession.getId());

        final List<CommentDto.Response> comments = commentService.findAllByArticleId(articleId);
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }
}
