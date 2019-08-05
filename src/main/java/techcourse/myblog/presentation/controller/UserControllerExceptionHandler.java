package techcourse.myblog.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.exception.DuplicatedEmailException;
import techcourse.myblog.application.exception.LoginFailedException;
import techcourse.myblog.application.exception.NotFoundArticleException;
import techcourse.myblog.application.exception.NotFoundCommentException;
import techcourse.myblog.domain.article.exception.MismatchArticleAuthorException;
import techcourse.myblog.domain.comment.exception.InvalidCommentException;
import techcourse.myblog.domain.comment.exception.MismatchCommentAuthorException;
import techcourse.myblog.presentation.support.exception.CustomException;

@ControllerAdvice
public class UserControllerExceptionHandler {
    @ExceptionHandler(LoginFailedException.class)
    public RedirectView handleLoginException(LoginFailedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public RedirectView handleSignupException(DuplicatedEmailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler({MismatchArticleAuthorException.class, MismatchCommentAuthorException.class})
    public ResponseEntity<CustomException> handleMismatchAuthorException(RuntimeException e) {
        return new ResponseEntity<>(new CustomException(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({NotFoundArticleException.class, NotFoundCommentException.class})
    public ResponseEntity<CustomException> handleNotFoundException(RuntimeException e) {
        return new ResponseEntity<>(new CustomException(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCommentException.class)
    public RedirectView handleCommentException(InvalidCommentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userDto", new UserDto("", "", ""));
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRequestDto", e.getBindingResult());
        return new RedirectView(e.getObjectName());
    }
}