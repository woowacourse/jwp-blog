package techcourse.myblog.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class InterceptorExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(InterceptorExceptionHandler.class);

    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e) {
        log.error(e.getMessage());
    }
}
