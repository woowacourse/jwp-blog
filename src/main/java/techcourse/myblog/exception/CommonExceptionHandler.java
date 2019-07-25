package techcourse.myblog.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CommonExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(NotFoundObjectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotFoundException(NotFoundObjectException e) {
        log.error(e.getMessage());
        return "redirect:/";
    }


    @ExceptionHandler(UnacceptablePathException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUnacceptablePathException(UnacceptablePathException e) {
        log.error(e.getMessage());
        return "redirect:/login";
    }


}