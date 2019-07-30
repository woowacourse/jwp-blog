package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

// TODO URI 패스를 constant로 분리하자~
@Controller
public class CommentController {
    private final HttpSession httpSession;
    private final CommentService commentService;

    public CommentController(HttpSession httpSession, CommentService commentService) {
        this.httpSession = httpSession;
        this.commentService = commentService;
    }

    @PostMapping("/comment/{articleId}")
    public String saveComment(@PathVariable Long articleId, @Valid CommentRequestDto commentRequestDto) {
        UserResponseDto userResponseDto = (UserResponseDto) SessionUtil.getAttribute(httpSession, USER);
        commentService.addComment(commentRequestDto, userResponseDto, articleId);

        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/comment/{articleId}/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentRequestDto commentRequestDto) {
        UserResponseDto userResponseDto = (UserResponseDto) SessionUtil.getAttribute(httpSession, USER);
        commentService.checkAuthentication(userResponseDto, commentId);
        commentService.update(commentId, commentRequestDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comment/{articleId}/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        UserResponseDto userResponseDto = (UserResponseDto) SessionUtil.getAttribute(httpSession, USER);
        commentService.checkAuthentication(userResponseDto, commentId);
        commentService.remove(commentId);
        return "redirect:/articles/" + articleId;
    }

}
