package techcourse.myblog.web.support;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.CommentUpdateFailedException;
import techcourse.myblog.domain.InvalidCommentException;
import techcourse.myblog.service.LoginFailedException;
import techcourse.myblog.service.MismatchAuthorException;
import techcourse.myblog.service.NotFoundArticleException;
import techcourse.myblog.service.SignUpFailedException;
import techcourse.myblog.web.controller.*;

@ControllerAdvice
public class MyBlogExceptionHandler {
    @ExceptionHandler(SignUpBindException.class)
    public RedirectView handleSignUpBindException(SignUpBindException e, RedirectAttributes redirectAttributes) {
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/signup");
    }

    @ExceptionHandler(LoginBindException.class)
    public RedirectView handleLoginBindException(LoginBindException e, RedirectAttributes redirectAttributes) {
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/login");
    }

    @ExceptionHandler(EditUserBindException.class)
    public RedirectView handleEditUserBindException(EditUserBindException e, RedirectAttributes redirectAttributes) {
        setBindExceptionRedirectAttributes(e, redirectAttributes);
        return new RedirectView("/mypage/edit");
    }

    @ExceptionHandler(CreateArticleBindException.class)
    public RedirectView handleEditUserBindException(CreateArticleBindException e, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler(LoginFailedException.class)
    public RedirectView handleLoginFail(LoginFailedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler(MismatchAuthorException.class)
    public RedirectView handleMismatchArticleAuthorFail(MismatchAuthorException e, RedirectAttributes redirectAttributes) {
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(NotFoundArticleException.class)
    public RedirectView handleArticleFail(NotFoundArticleException e, RedirectAttributes redirectAttributes) {
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(InvalidCommentException.class)
    public RedirectView handleCommentFail(InvalidCommentException e, RedirectAttributes redirectAttributes) {
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(CommentUpdateFailedException.class)
    public RedirectView handleUpdateCommentFail(CommentUpdateFailedException e, RedirectAttributes redirectAttributes) {
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    public RedirectView handleUnauthorizedRequest(UnauthorizedRequestException e, RedirectAttributes redirectAttributes) {
        return redirectToRootWithErrorMsg(redirectAttributes, e.getMessage());
    }

    private RedirectView redirectToRootWithErrorMsg(RedirectAttributes redirectAttributes, String errorMsg) {
        redirectAttributes.addFlashAttribute("error", errorMsg);
        return new RedirectView("/");
    }
}