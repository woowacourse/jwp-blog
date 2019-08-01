package techcourse.myblog.web.controllerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.exception.UserNotFoundException;

@ControllerAdvice
public class LoginControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(LoginControllerAdvice.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String emailNotFound() {
        log.debug("이메일이 없습니다.");
        return "login";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String invalidPassword() {
        log.debug("비밀번호가 일치하지 않습니다.");
        return "login";
    }
}
