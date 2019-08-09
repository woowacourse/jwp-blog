package techcourse.myblog.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.service.exception.AuthException;

@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public String handleAuthException(Exception e, Model model) {

        log.error(e.toString());

        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("path", "/");
        return "error";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleRuntimeException(Exception e, Model model) {

        log.error(e.toString());

        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("path", "/");
        return "error";
    }


}
