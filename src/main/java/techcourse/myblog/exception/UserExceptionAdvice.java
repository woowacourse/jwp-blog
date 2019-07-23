package techcourse.myblog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class UserExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(UserExceptionAdvice.class);
    private static final String ERROR_MESSAGE = "errorMessage";

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, final Model model) {
        log.info(loggingException(e));
        model.addAttribute(ERROR_MESSAGE, "존재하지 않습니다");
        return "index";
    }

    @ExceptionHandler(UserCreationException.class)
    public String handleUserCreationException(UserCreationException e, final Model model) {
        log.info(loggingException(e));
        model.addAttribute(ERROR_MESSAGE, e.getMessage());
        return e.getUrl();
    }

    @ExceptionHandler(UserUpdateException.class)
    public String handleUserUpdateException(UserUpdateException e, final Model model) {
        log.info(loggingException(e));
        model.addAttribute(ERROR_MESSAGE, e.getMessage());
        return e.getUrl();
    }

    private String loggingException(Exception e) {
        return e.getClass() + " occured, message : {}" + e.getMessage();
    }
}
