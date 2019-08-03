package techcourse.myblog.web.controller;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.CommentUpdateFailedException;
import techcourse.myblog.domain.InvalidCommentException;
import techcourse.myblog.service.DuplicatedEmailException;
import techcourse.myblog.service.MismatchAuthorException;
import techcourse.myblog.service.NotFoundArticleException;

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

    @ExceptionHandler(DuplicatedEmailException.class)
    public RedirectView handleSignupFail(DuplicatedEmailException e, RedirectAttributes redirectAttributes) {
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
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(NotFoundArticleException.class)
    public RedirectView handleArticleFail(NotFoundArticleException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(InvalidCommentException.class)
    public RedirectView handleCommentFail(InvalidCommentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }

    @ExceptionHandler(CommentUpdateFailedException.class)
    public RedirectView handleUpdateCommentFail(CommentUpdateFailedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/");
    }
}