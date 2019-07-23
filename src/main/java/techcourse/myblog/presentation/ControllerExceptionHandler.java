package techcourse.myblog.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.service.AuthenticationFailException;
import techcourse.myblog.service.ArticleNotFoundException;

import javax.servlet.http.HttpServletResponse;

import static techcourse.myblog.presentation.UserController.LOGIN_ERROR_MSG;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException e, HttpServletResponse response) {
        response.setStatus(404);
        return "error404";
    }

    @ExceptionHandler(AuthenticationFailException.class)
    public String handleUserNotFoundException(AuthenticationFailException e, HttpServletResponse response, Model model) {
        model.addAttribute("error", LOGIN_ERROR_MSG);
        response.setStatus(404);
        return "login";
    }
}
