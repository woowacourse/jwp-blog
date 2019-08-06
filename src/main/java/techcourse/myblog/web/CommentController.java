package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.comment.CommentRequest;
import techcourse.myblog.dto.comment.CommentResponse;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;
import static techcourse.myblog.web.URL.COMMENT;

@Controller
public class CommentController {

    private final HttpSession session;
    private final CommentService commentService;

    public CommentController(HttpSession session, CommentService commentService) {
        this.session = session;
        this.commentService = commentService;
    }

    @PostMapping(COMMENT + "/{articleId}")
    @ResponseBody
    public CommentResponse saveComment(@PathVariable Long articleId, @RequestBody @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);

        return commentService.addComment(commentRequest, userResponse, articleId);
    }

    @PutMapping(COMMENT + "/{articleId}" + "/{commentId}")
    @ResponseBody
    public CommentResponse updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);

        return commentService.update(commentId, commentRequest, userResponse);
    }

    @DeleteMapping(COMMENT + "/{articleId}" + "/{commentId}")
    @ResponseBody
    public void deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.delete(commentId, userResponse);
    }

}
