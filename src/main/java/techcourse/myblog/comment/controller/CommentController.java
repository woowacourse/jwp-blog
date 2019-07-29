package techcourse.myblog.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.comment.dto.CommentDto;
import techcourse.myblog.comment.exception.InvalidCommentLengthException;
import techcourse.myblog.comment.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public RedirectView createComment(@PathVariable long articleId, UserSession userSession, CommentDto.Creation commentDto) {
        commentService.save(articleId, userSession.getId(), commentDto);
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public RedirectView updateComment(@PathVariable long articleId, @PathVariable long commentId, UserSession userSession,
                                      @Valid CommentDto.Updation commentDto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            request.setAttribute("articleId", articleId);
            throw new InvalidCommentLengthException();
        }
        commentService.update(commentId, userSession.getId(), commentDto);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public RedirectView deleteComment(@PathVariable long articleId, @PathVariable long commentId, UserSession userSession) {
        commentService.delete(commentId, userSession.getId());
        return new RedirectView("/articles/" + articleId);
    }
}
