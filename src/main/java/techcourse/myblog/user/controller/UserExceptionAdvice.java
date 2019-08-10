package techcourse.myblog.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.user.exception.*;

@ControllerAdvice
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

    @ExceptionHandler(DuplicatedUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handelDuplicatedUserException(DuplicatedUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/users/new";
    }

    @ExceptionHandler({InvalidLoginFormException.class, NotFoundUserException.class, NotMatchPasswordException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLoginException(RuntimeException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "/login";
    }

    @ExceptionHandler(AuthenticationFailException.class)
    public RedirectView handleAuthenticationFailException(AuthenticationFailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return new RedirectView("/");
    }
}