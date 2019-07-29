package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.UserResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public String saveComment(@PathVariable("articleId") Long articleId, @Valid CommentRequest commentRequest,
                              BindingResult bindingResult, HttpSession httpSession) {
        if(!bindingResult.hasErrors()) {
            UserResponse user = (UserResponse)httpSession.getAttribute("user");
            commentService.save(commentRequest, articleId, user.getId());
        }

        return "redirect:/articles/" + articleId;
    }
}
