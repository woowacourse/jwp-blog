package techcourse.myblog.comment.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.comment.exception.CommentNotFoundException;

@ControllerAdvice
public class CommentControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(CommentControllerAdvice.class);

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String commentNotFound() {
        log.debug("존재하지 않는 댓글입니다.");
        return "index";
    }
}
