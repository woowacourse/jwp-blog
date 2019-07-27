package techcourse.myblog.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.EmailDuplicatedException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserArgumentException;
import techcourse.myblog.exception.UserNotFoundException;

@ControllerAdvice
public class UserAdvice {

    @ExceptionHandler(EmailDuplicatedException.class)
    public String handleException(EmailDuplicatedException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserArgumentException.class)
    public String handleException(UserArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleException(UserNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }

    @ExceptionHandler(LoginFailException.class)
    public String handleException(LoginFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "login";
    }
}
