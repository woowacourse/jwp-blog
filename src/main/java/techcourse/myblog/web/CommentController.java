package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.comment.CommentService;
import techcourse.myblog.service.dto.comment.CommentRequest;
import techcourse.myblog.service.dto.comment.CommentResponse;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

import java.util.List;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @PostMapping("")
    public List<CommentResponse> addComment(@PathVariable final Long articleId, @RequestBody final CommentRequest commentRequest, final AccessUserInfo accessUserInfo) {
        log.info("Json : " + commentRequest.getComment());
        commentService.save(commentRequest, accessUserInfo.getUser().getId(), articleId);
        return commentService.findByArticleId(articleId);
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
