package techcourse.myblog.presentation.controller;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.service.exception.*;

import java.util.stream.Collectors;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(DuplicatedIdException.class)
    public RedirectView handleDuplicatedIdError(RedirectAttributes redirectAttributes, DuplicatedIdException e) {
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());

        return new RedirectView("/signup");
    }

    @ExceptionHandler(NotExistUserIdException.class)
    public RedirectView handleNotExistIdError(RedirectAttributes redirectAttributes, NotExistUserIdException e) {
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());

        return new RedirectView("/login");
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public RedirectView handleNotMatchPasswordError(RedirectAttributes redirectAttributes, NotMatchPasswordException e) {
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());

        return new RedirectView("/login");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(RedirectAttributes redirectAttributes, BindException e) {
        String errorMessages = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        redirectAttributes.addFlashAttribute("errormessage", errorMessages);
        return new RedirectView("signup");
    }

    @ExceptionHandler(NotExistCommentException.class)
    public RedirectView handleNotMatchCommentUserError(RedirectAttributes redirectAttributes, NotExistCommentException e) {
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());

        return  new RedirectView("/");
    }

    @ExceptionHandler(NotMatchEmailException.class)
    public RedirectView handleNotMatchEmailError(RedirectAttributes redirectAttributes, NotMatchEmailException e) {
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());

        return new RedirectView("/");
    }
}
