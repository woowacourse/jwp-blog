package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.UserSession;
import techcourse.myblog.web.view.CommentResponse;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentApi {
    private static final Logger log = LoggerFactory.getLogger(CommentApi.class);

    private final CommentService commentService;

    public CommentApi(final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto.Response>> list(@PathVariable Long articleId) {
        log.debug("articleId:{}", articleId);
        final List<CommentDto.Response> comments = commentService.findAllByArticleId(articleId);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@PathVariable Long articleId,
                                                  @RequestBody CommentDto.Create commentCreate,
                                                  UserSession userSession) {
        log.debug("contents: {}", commentCreate.getContents());

        commentCreate.setArticleId(articleId);
        final CommentDto.Response savedComment = commentService.save(commentCreate, userSession.getId());

        // TODO 생성 방식 수정
        CommentResponse commentResponse = new CommentResponse(savedComment.getId(), "등록 성공");
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> delete(@PathVariable Long articleId,
                                                  @PathVariable Long commentId,
                                                  UserSession userSession) {
        log.debug("commentId: {}", commentId);

        commentService.delete(commentId, userSession.getId());

        CommentResponse commentResponse = new CommentResponse(null, "삭제 성공");
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(@PathVariable Long articleId,
                                                  @PathVariable Long commentId,
                                                  @RequestBody CommentDto.Update commentUpdate,
                                                  UserSession userSession) {
        log.debug("commentId: {}", commentId);

        commentUpdate.setArticleId(articleId);
        commentUpdate.setId(commentId);
        commentService.update(commentUpdate, userSession.getId());

        CommentResponse commentResponse = new CommentResponse(null, "수정 성공");
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }
}
