package techcourse.myblog.controller.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.*;

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

    @ExceptionHandler(UserUpdateFailException.class)
    public String handleException(UserUpdateFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "mypage-edit";
    }
}
