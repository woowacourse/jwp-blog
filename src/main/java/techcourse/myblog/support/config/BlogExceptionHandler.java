package techcourse.myblog.support.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.service.exception.EditException;
import techcourse.myblog.service.exception.ErrorMessage;
import techcourse.myblog.service.exception.LoginException;

@ControllerAdvice
public class BlogExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public String handleLoginException(LoginException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        return "login";
    }

    @ExceptionHandler(EditException.class)
    public String handleEditException(EditException e, Model model) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        model.addAttribute("error", errorMessage);
        return "mypage-edit";
    }
}
