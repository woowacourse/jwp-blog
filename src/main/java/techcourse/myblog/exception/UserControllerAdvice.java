package techcourse.myblog.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler({UserDuplicateException.class, UserMismatchException.class, UserNotFoundException.class})
    public String handleUserException(IllegalArgumentException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(IllegalUserParamsException.class)
    public String handleIllegalUserParamsException(IllegalUserParamsException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
