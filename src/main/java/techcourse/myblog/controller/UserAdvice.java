package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserFromException;
import techcourse.myblog.exception.UserNotExistException;

@ControllerAdvice
public class UserAdvice {
    @ExceptionHandler(EmailRepetitionException.class)
    public String handleException(EmailRepetitionException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(UserFromException.class)
    public String handleException(UserFromException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(UserNotExistException.class)
    public String handleException(UserNotExistException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }

    @ExceptionHandler(LoginFailException.class)
    public String handleException(LoginFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }

}
