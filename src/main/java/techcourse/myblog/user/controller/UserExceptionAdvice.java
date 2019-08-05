package techcourse.myblog.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.user.exception.*;
import techcourse.myblog.utils.Error;

@ControllerAdvice(value = "techcourse.myblog.user.controller")
public class UserExceptionAdvice {
    @ExceptionHandler(InvalidSignUpFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidSignUpFormException(InvalidSignUpFormException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(InvalidEditFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelInvalidEditFormException(InvalidEditFormException e, UserSession session, Model model) {
        model.addAttribute("user", session);
        model.addAttribute("errorMessage", e.getMessage());
        return "mypage";
    }

    @ExceptionHandler(InvalidLoginFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleInvalidLoginException(InvalidLoginFormException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelDuplicatedUserException(DuplicatedUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleNotFoundUserException(NotFoundUserException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleNotMatchPasswordException(NotMatchPasswordException e) {
        return new Error(e.getMessage());
    }
}