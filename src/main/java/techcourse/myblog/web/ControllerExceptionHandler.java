package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.domain.ArticleException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ArticleException.class)
    public String handleGetArticleException(ArticleException e) {
        log.info("handleGetArticleException : {} ", e.getMessage());
        return "/";
    }
}
