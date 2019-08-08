package techcourse.myblog.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.comment.dto.CommentRequest;
import techcourse.myblog.comment.dto.CommentResponse;
import techcourse.myblog.comment.service.CommentService;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

import java.util.List;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public List<CommentResponse> addComment(@PathVariable final Long articleId, @RequestBody final CommentRequest commentRequest, final AccessUserInfo accessUserInfo) {
        commentService.save(commentRequest, accessUserInfo.getUser().getId(), articleId);
        return commentService.findByArticleId(articleId);
    }


    @PutMapping("/{commentId}")
    public List<CommentResponse> updateComment(@PathVariable final Long articleId, @PathVariable final Long commentId, @RequestBody final CommentRequest commentRequest, final AccessUserInfo accessUserInfo) {
        commentService.update(commentRequest, commentId, articleId, accessUserInfo.getUser());
        return commentService.findByArticleId(articleId);
    }

    @DeleteMapping("/{commentId}")
    public List<CommentResponse> deleteComment(@PathVariable final Long articleId, @PathVariable final Long commentId, final AccessUserInfo accessUserInfo) {
        commentService.delete(commentId, accessUserInfo.getUser());
        return commentService.findByArticleId(articleId);
    }
}
