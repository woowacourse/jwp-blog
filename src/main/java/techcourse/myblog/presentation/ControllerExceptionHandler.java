package techcourse.myblog.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.service.exception.AuthenticationFailException;
import techcourse.myblog.service.exception.ArticleNotFoundException;

import javax.servlet.http.HttpServletResponse;

import static techcourse.myblog.presentation.UserController.LOGIN_ERROR_MSG;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleArticleNotFoundException(ArticleNotFoundException e, HttpServletResponse response) {
        return "error404";
    }

    @ExceptionHandler(AuthenticationFailException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(AuthenticationFailException e, HttpServletResponse response, Model model) {
        log.info("called..!!");

        model.addAttribute("error", LOGIN_ERROR_MSG);
        return "login";
    }
}
