package techcourse.myblog.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.exception.UnauthorizedException;

@Slf4j
@ControllerAdvice
public class UnauthorizedExceptionAdvice {
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedExceptionHandler(UnauthorizedException e) {
        log.debug(e.getMessage());
        return e.getMessage();
    }
}
