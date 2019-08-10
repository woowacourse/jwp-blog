package techcourse.myblog.comment.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.comment.exception.InvalidCommentLengthException;
import techcourse.myblog.comment.exception.NotFoundCommentException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CommentExceptionAdvice {
    @ExceptionHandler(NotFoundCommentException.class)
    public RedirectView handleNotFoundCommentException(NotFoundCommentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(InvalidCommentLengthException.class)
    public RedirectView handleInvalidCommentLengthException(
            InvalidCommentLengthException e, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/articles/" + request.getAttribute("articleId"));
    }
}
