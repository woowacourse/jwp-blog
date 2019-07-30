package techcourse.myblog.presentation.controller;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;

import java.util.stream.Collectors;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(DuplicatedIdException.class)
    public RedirectView handleDuplicatedIdError(RedirectAttributes redirectAttributes, DuplicatedIdException e) {
        RedirectView redirectView = new RedirectView("/signup");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotExistUserIdException.class)
    public RedirectView handleNotExistIdError(RedirectAttributes redirectAttributes, NotExistUserIdException e) {
        RedirectView redirectView = new RedirectView("/login");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public RedirectView handleNotMatchPasswordError(RedirectAttributes redirectAttributes, NotMatchPasswordException e) {
        RedirectView redirectView = new RedirectView("/login");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(RedirectAttributes redirectAttributes, BindException e) {
        RedirectView redirectView = new RedirectView("signup");

        String errorMessages = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        redirectAttributes.addFlashAttribute("errormessage", errorMessages);
        return redirectView;
    }
}
