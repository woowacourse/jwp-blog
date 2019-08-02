package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private CommentService commentService;
    private LoginChecker loginChecker;

    public CommentController(CommentService commentService, LoginChecker loginChecker) {
        this.commentService = commentService;
        this.loginChecker = loginChecker;
    }

    @PostMapping("/comment")
    public String createComment(CommentRequestDto commentRequestDto, HttpSession session) {
        commentService.save(loginChecker.getLoggedInUser(session), commentRequestDto);
        return "redirect:/articles/" + commentRequestDto.getArticleId();
    }

    @PutMapping("/articles/{articleId}/comment/{commentId}")
    public String updateComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
                                CommentRequestDto commentRequestDto, HttpSession session) {
        // commentId, commentRequestDto
        commentService.update(loginChecker.getLoggedInUser(session), commentId, commentRequestDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId,
                                HttpSession session) {
        commentService.delete(loginChecker.getLoggedInUser(session), commentId);
        return "redirect:/articles/" + articleId;
    }
}
