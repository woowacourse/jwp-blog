package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    @ResponseBody
    public CommentResponse save(@PathVariable("articleId") Long articleId,
                                @RequestBody CommentRequest commentRequest, HttpSession httpSession) {
        log.info(commentRequest.getContents());

        UserResponse user = (UserResponse) httpSession.getAttribute("user");
        return commentService.save(commentRequest, articleId, user.getId());
    }

    @PutMapping("/{commentId}")
    @ResponseBody
    public CommentResponse modify(@PathVariable("commentId") Long commentId,
                                  @RequestBody CommentRequest commentRequest, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        return commentService.modify(commentId, userResponse, commentRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseBody
    public void remove(@PathVariable("commentId") Long commentId, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        commentService.remove(commentId, userResponse);
    }
}
