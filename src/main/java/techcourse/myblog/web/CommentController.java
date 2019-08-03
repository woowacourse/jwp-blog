package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.UserResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/articles/{articleId}")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public String saveComment(@PathVariable("articleId") Long articleId, @Valid CommentRequest commentRequest,
                              BindingResult bindingResult, HttpSession httpSession) {
        if (!bindingResult.hasErrors()) {
            UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
            commentService.save(commentRequest, articleId, userResponse.getId());
        }

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comments/{commentId}")
    public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        commentService.deleteComment(commentId, userResponse);

        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comments/{commentId}")
    public String updateComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId, CommentRequest commentRequest, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        commentService.updateComment(commentId, userResponse, commentRequest);

        return "redirect:/articles/" + articleId;
    }
}
