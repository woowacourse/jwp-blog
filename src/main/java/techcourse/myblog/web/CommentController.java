package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.comment.CommentRequest;
import techcourse.myblog.dto.comment.CommentResponse;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.utils.custum.LoginUser;

import javax.validation.Valid;

import static techcourse.myblog.web.URL.COMMENT;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(COMMENT + "/{articleId}")
    @ResponseBody
    public CommentResponse saveComment(@PathVariable Long articleId, @RequestBody @Valid CommentRequest commentRequest
            , @LoginUser UserResponse loginUser) {

        return commentService.addComment(commentRequest, loginUser, articleId);
    }

    @PutMapping(COMMENT + "/{articleId}" + "/{commentId}")
    @ResponseBody
    public CommentResponse updateComment(@PathVariable Long articleId, @PathVariable Long commentId
            , @RequestBody @Valid CommentRequest commentRequest, @LoginUser UserResponse userResponse) {

        return commentService.update(commentId, commentRequest, userResponse);
    }

    @DeleteMapping(COMMENT + "/{articleId}" + "/{commentId}")
    @ResponseBody
    public void deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, @LoginUser UserResponse loginUser) {
        commentService.delete(commentId, loginUser);
    }

}
