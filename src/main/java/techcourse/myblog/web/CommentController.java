package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public String createComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Long userId = getLoggedInUser(httpServletRequest).getId();
        commentService.save(userId, commentRequestDto);
        return "redirect:/articles/" + commentRequestDto.getArticleId();
    }

    @PutMapping("/comment/{id}")
    public String updateComment(@PathVariable("id") Long commentId,
                                CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        Long userId = getLoggedInUser(httpServletRequest).getId();
        commentService.update(userId, commentId, commentRequestDto);
        return "redirect:/articles/" + commentRequestDto.getArticleId();
    }

    private UserPublicInfoDto getLoggedInUser(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserPublicInfoDto user = (UserPublicInfoDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
    }
}
