package techcourse.myblog.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentControllerAdvice {

    @ExceptionHandler(IllegalCommentUpdateRequestException.class)
    public String handleArticleNotFoundException() {
        return "error";
    }
}
