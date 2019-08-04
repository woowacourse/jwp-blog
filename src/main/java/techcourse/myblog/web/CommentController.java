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
import static techcourse.myblog.web.URL.*;

@Controller
public class CommentController {

    private final HttpSession session;
    private final CommentService commentService;

    public CommentController(HttpSession session, CommentService commentService) {
        this.session = session;
        this.commentService = commentService;
    }

    @PostMapping(COMMENT + "/{articleId}")
    public String saveComment(@PathVariable Long articleId, @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.addComment(commentRequest, userResponse, articleId);

        return REDIRECT + ARTICLES + "/" + articleId;
    }

    @PutMapping(COMMENT + "/{articleId}" + "/{commentId}")
    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @Valid CommentRequest commentRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.update(commentId, commentRequest, userResponse);

        return REDIRECT + ARTICLES + "/" + articleId;
    }

    @DeleteMapping(COMMENT + "/{articleId}" + "/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        commentService.delete(commentId, userResponse);

        return REDIRECT + ARTICLES + "/" + articleId;
    }

}
