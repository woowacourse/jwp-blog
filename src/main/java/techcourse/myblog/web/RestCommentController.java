package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class RestCommentController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public List<CommentResponseDto> readComments(@PathVariable("id") Long id) {
        return commentService.findCommentsByArticleId(id);
    }

    @PostMapping("/comments")
    @ResponseStatus(value = HttpStatus.OK)
    public void createComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Long userId = getLoggedInUser(httpServletRequest).getId();
        commentService.save(userId, commentRequestDto);
    }

    @PutMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateComment(@PathVariable("commentId") Long commentId,
                             @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {

        Long userId = getLoggedInUser(httpServletRequest).getId();
        commentService.update(userId, commentId, commentRequestDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteComment(@PathVariable("commentId") Long commentId, HttpServletRequest httpServletRequest) {
        Long userId = getLoggedInUser(httpServletRequest).getId();
        commentService.delete(userId, commentId);
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
