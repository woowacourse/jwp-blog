package techcourse.myblog.presentation.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;

import java.util.stream.Collectors;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(DuplicatedIdException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RedirectView handleDuplicatedIdError(RedirectAttributes redirectAttributes, DuplicatedIdException e) {
        RedirectView redirectView = new RedirectView("/signup");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotExistUserIdException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RedirectView handleNotExistIdError(RedirectAttributes redirectAttributes, NotExistUserIdException e) {
        RedirectView redirectView = new RedirectView(e.getNextView());
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotExistArticleIdException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RedirectView handleNotExistArticleIdError(RedirectAttributes redirectAttributes, NotExistArticleIdException e) {
        RedirectView redirectView = new RedirectView("/");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RedirectView handleNotMatchPasswordError(RedirectAttributes redirectAttributes, NotMatchPasswordException e) {
        RedirectView redirectView = new RedirectView("/login");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public RedirectView handleBindError(RedirectAttributes redirectAttributes, BindException e) {
        RedirectView redirectView = new RedirectView("signup");

        String errorMessages = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        redirectAttributes.addFlashAttribute("errormessage", errorMessages);
        return redirectView;
    }
}
