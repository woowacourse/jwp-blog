package techcourse.myblog.support.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.service.dto.UserEditRequest;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.ErrorMessage;
import techcourse.myblog.service.exception.IncludeRedirectUrlException;
import techcourse.myblog.service.exception.LoginException;

@ControllerAdvice
public class MyBlogExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public String handleLoginException(LoginException e, Model model) {
        model.addAttribute("error", new ErrorMessage(e.getMessage()));
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "login";
    }

    @ExceptionHandler(EditException.class)
    public String handleEditException(EditException e, Model model) {
        model.addAttribute("error", new ErrorMessage(e.getMessage()));
        model.addAttribute("userEditRequest", new UserEditRequest());
        return "mypage-edit";
    }

    @ExceptionHandler(IncludeRedirectUrlException.class)
    public String handleSignUpException(IncludeRedirectUrlException e, Model model) {
        model.addAttribute("error", new ErrorMessage(e.getMessage()));
        return e.getRedirectUrl();
    }
}
