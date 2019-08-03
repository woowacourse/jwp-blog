package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

@Controller
public class CommentController {
    private final HttpSession session;
    private final CommentService commentService;

    public CommentController(HttpSession session, CommentService commentService) {
        this.session = session;
        this.commentService = commentService;
    }

    @PostMapping("/comment/{articleId}")
    public String saveComment(@PathVariable Long articleId, @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.addComment(commentRequest, userResponse, articleId);

        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comment/{articleId}/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.checkAuthentication(userResponse, commentId);
        commentService.update(commentId, commentRequest);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comment/{articleId}/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.checkAuthentication(userResponse, commentId);
        commentService.remove(commentId);

        return "redirect:/articles/" + articleId;
    }

}
