package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.comment.CommentService;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public String addComment(@PathVariable final Long articleId, final CommentRequest commentRequest, final AccessUserInfo accessUserInfo) {
        commentService.save(commentRequest, accessUserInfo.getUser().getId(), articleId);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable final Long articleId, @PathVariable final Long commentId, final CommentRequest commentRequest, final AccessUserInfo accessUserInfo) {
        commentService.update(commentRequest, commentId, articleId, accessUserInfo.getUser());
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable final Long articleId, @PathVariable final Long commentId, final AccessUserInfo accessUserInfo) {
        commentService.delete(commentId, accessUserInfo.getUser());
        return "redirect:/articles/" + articleId;
    }
}
