package techcourse.myblog.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.user.exception.*;
import techcourse.myblog.errors.Error;

@ControllerAdvice(value = "techcourse.myblog.user.controller")
public class UserExceptionAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidSignUpFormException.class)
    public Error handleInvalidSignUpFormException(InvalidSignUpFormException e) {
        return new Error(e.getMessage());
    }

    @ExceptionHandler(InvalidEditFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelInvalidEditFormException(InvalidEditFormException e, UserSession session, Model model) {
        model.addAttribute("user", session);
        model.addAttribute("errorMessage", e.getMessage());
        return "mypage";
    }

    @ResponseBody
    @ExceptionHandler(InvalidLoginFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleInvalidLoginException(InvalidLoginFormException e) {
        return new Error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handelDuplicatedUserException(DuplicatedUserException e) {
        return new Error(e.getMessage());
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