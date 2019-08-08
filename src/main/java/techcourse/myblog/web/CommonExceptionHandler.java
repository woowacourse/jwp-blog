package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.domain.InvalidAuthorException;
import techcourse.myblog.service.exception.NotFoundObjectException;

@ControllerAdvice
public class CommonExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(NotFoundObjectException.class)
    public String handleNotFoundException(NotFoundObjectException e) {
        log.error(e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(InvalidAuthorException.class)
    public String handleInvalidAuthorException(InvalidAuthorException e) {
        log.error(e.getMessage());
        return "redirect:/";
    }
}