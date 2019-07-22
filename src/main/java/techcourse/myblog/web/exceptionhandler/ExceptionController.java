package techcourse.myblog.web.exceptionhandler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.web.exception.CouldNotRegisterException;
import techcourse.myblog.web.exception.LoginFailException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundUserException.class)
    public String abc(NotFoundUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(LoginFailException.class)
    public String failLogin(LoginFailException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(CouldNotRegisterException.class)
    public String failSignUp(CouldNotRegisterException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
