package techcourse.myblog.support.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.service.dto.UserEditRequest;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.ErrorMessage;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.service.exception.NoArticleException;

@ControllerAdvice
public class BlogExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public String handleLoginException(LoginException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        model.addAttribute("userLoginRequest", new UserLoginRequest());
        return "login";
    }

    @ExceptionHandler(EditException.class)
    public String handleEditException(EditException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        model.addAttribute("userEditRequest", new UserEditRequest());
        return "mypage-edit";
    }

    @ExceptionHandler(NoArticleException.class)
    public String handleNoArticleException(NoArticleException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        return "404";
    }
}
