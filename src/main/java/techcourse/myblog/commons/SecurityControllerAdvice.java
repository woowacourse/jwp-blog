package techcourse.myblog.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.users.ValidSingupException;

@ControllerAdvice(annotations = Controller.class)
public class SecurityControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(SecurityControllerAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String validation() {
        log.debug("MethodArgumentNotValidException is happened!");
        return "redirect:/";
    }

    @ExceptionHandler(ValidSingupException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String inValidSingup() {
        log.debug("MethodArgumentNotValidException is happened!");
        return "redirect:/users/login";
    }
}