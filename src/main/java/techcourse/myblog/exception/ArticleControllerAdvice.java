package techcourse.myblog.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArticleControllerAdvice {

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException() {
        return "error";
    }
}
