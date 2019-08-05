package techcourse.myblog.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.CommentDeleteException;
import techcourse.myblog.exception.CommentUpdateException;

@ControllerAdvice
public class CommentControllerAdvice {

    @ExceptionHandler(CommentUpdateException.class)
    public String commentUpdateFailed() {
        return "redirect:/";
    }

    @ExceptionHandler(CommentDeleteException.class)
    public String commentDeleteFailed() {
        return "redirect:/";
    }
}
