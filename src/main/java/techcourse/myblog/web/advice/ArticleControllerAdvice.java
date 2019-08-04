package techcourse.myblog.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;

@ControllerAdvice
public class ArticleControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ArticleControllerAdvice.class);

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String articleNotFound() {
        log.debug("존재하지 않는 게시글입니다.");
        return "index";
    }

    @ExceptionHandler(ArticleToUpdateNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String articleToUpdateNotFound() {
        log.debug("업데이트 해야할 게시글이 없습니다.");
        return "index";
    }
}
