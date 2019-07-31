package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.domain.user.LoginException;
import techcourse.myblog.domain.user.UserException;

@ControllerAdvice(annotations = Controller.class)
public class LoginControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(LoginControllerAdvice.class);

    @ExceptionHandler(UserException.class)
    @ResponseStatus(value = HttpStatus.FOUND)
    public String userHandler(UserException e, Model model) {
        log.debug("LOGIN FAILED {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "redirect:/error";
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String failToLogin(LoginException e, Model model) {
        log.debug("LOGIN FAILED {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "login";
    }
}
