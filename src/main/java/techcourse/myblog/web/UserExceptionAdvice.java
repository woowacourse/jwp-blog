package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchPasswordException;

@ControllerAdvice
public class UserExceptionAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelBindingException(BindException e, Model model) {
        model.addAttribute("errorMessage", e.getFieldError().getDefaultMessage());
        return "signup";
    }

    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelDuplicatedUserException(DuplicatedUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/signup";
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundUserException(NotFoundUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/login";
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotMatchPasswordException(NotMatchPasswordException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/login";
    }
}
