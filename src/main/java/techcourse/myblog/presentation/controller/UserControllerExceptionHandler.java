package techcourse.myblog.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.exception.DuplicatedEmailException;
import techcourse.myblog.application.exception.LoginFailedException;
import techcourse.myblog.application.exception.NotFoundException;
import techcourse.myblog.domain.comment.exception.InvalidCommentException;
import techcourse.myblog.domain.exception.MismatchException;
import techcourse.myblog.presentation.support.RedirectUrl;
import techcourse.myblog.presentation.support.exception.CustomException;

@ControllerAdvice
public class UserControllerExceptionHandler {
    @ExceptionHandler(LoginFailedException.class)
    public RedirectView handleLoginException(LoginFailedException e,
                                             RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public RedirectView handleSignupException(DuplicatedEmailException e,
                                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler(MismatchException.class)
    public ResponseEntity<CustomException> handleMismatchAuthorException(MismatchException e) {
        return new ResponseEntity<>(new CustomException(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomException> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new CustomException(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCommentException.class)
    public RedirectView handleCommentException(InvalidCommentException e,
                                               RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e,
                                        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(e.getObjectName(), e.getTarget());
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + e.getObjectName(), e.getBindingResult());
        return new RedirectView(RedirectUrl.convert(e.getObjectName()).getUrl());
    }
}