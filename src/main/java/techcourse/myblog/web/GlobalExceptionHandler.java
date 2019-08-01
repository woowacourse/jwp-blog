package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidUserDataException(InvalidUserDataException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateEmailException(DuplicateEmailException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(FailedLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedLoginException(FailedLoginException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(FailedPasswordVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedPasswordVerificationException(FailedPasswordVerificationException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(InvalidAuthorException.class)
    public String handleInvalidAuthorException(InvalidAuthorException e) {
        return "redirect:/";
    }
}
