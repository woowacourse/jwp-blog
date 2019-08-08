package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.exception.CommentUpdateFailedException;
import techcourse.myblog.domain.exception.InvalidCommentException;
import techcourse.myblog.dto.ErrorResponse;
import techcourse.myblog.service.exception.*;
import techcourse.myblog.web.exception.*;

@ControllerAdvice
public class MyBlogExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(MyBlogExceptionHandler.class);

    @ExceptionHandler(SignUpBindException.class)
    public RedirectView handleSignUpBindException(SignUpBindException e, RedirectAttributes redirectAttributes) {
        logError(e);
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/signup");
    }

    @ExceptionHandler(LoginBindException.class)
    public RedirectView handleLoginBindException(LoginBindException e, RedirectAttributes redirectAttributes) {
        logError(e);
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/login");
    }

    @ExceptionHandler(EditUserBindException.class)
    public RedirectView handleEditUserBindException(EditUserBindException e, RedirectAttributes redirectAttributes) {
        logError(e);
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/mypage/edit");
    }

    @ExceptionHandler(CreateArticleBindException.class)
    public RedirectView handleEditUserBindException(CreateArticleBindException e, RedirectAttributes redirectAttributes) {
        logError(e);
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/writing");
    }

    private void setBindExceptionRedirectAttributes(BindException e, RedirectAttributes redirectAttributes) {
        String objectName = e.getObjectName();
        redirectAttributes.addFlashAttribute(objectName, e.getTarget());
        redirectAttributes.addFlashAttribute(String.format("org.springframework.validation.BindingResult.%s", objectName), e.getBindingResult());
    }

    @ExceptionHandler(SignUpFailedException.class)
    public RedirectView handleSignupFail(SignUpFailedException e, RedirectAttributes redirectAttributes) {
        logError(e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler(LoginFailedException.class)
    public RedirectView handleLoginFail(LoginFailedException e, RedirectAttributes redirectAttributes) {
        logError(e);
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    public RedirectView handleUnauthorizedRequest(UnauthorizedRequestException e, RedirectAttributes redirectAttributes) {
        logError(e);
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(MismatchAuthorException.class)
    public RedirectView handleMismatchArticleAuthorFail(MismatchAuthorException e, RedirectAttributes redirectAttributes) {
        logError(e);
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(NotFoundArticleException.class)
    public RedirectView handleArticleFail(NotFoundArticleException e, RedirectAttributes redirectAttributes) {
        logError(e);
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler({InvalidCommentException.class, CommentUpdateFailedException.class, NotFoundCommentException.class, MismatchCommentWriterException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentFail(RuntimeException e) {
        logError(e);
        return new ErrorResponse(e.getMessage());
    }

    private void logError(Exception e) {
        log.error("exception={}, error message={}", e.getClass().getName(), e.getMessage());
    }

    private RedirectView redirectToRootWithErrorMsg(RedirectAttributes redirectAttributes, String errorMsg) {
        redirectAttributes.addFlashAttribute("error", errorMsg);
        return new RedirectView("/");
    }
}