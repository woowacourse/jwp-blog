package techcourse.myblog.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ControllerExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException e, Model model) {
        return "error404";
    }
}
