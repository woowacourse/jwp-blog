package techcourse.myblog.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.application.service.exception.NotMatchCommentAuthorException;
import techcourse.myblog.presentation.response.StandardResponse;

@ControllerAdvice(basePackages = {"techcourse.myblog.presentation.controller"})
public class CommentExceptionHandlers {
    @ExceptionHandler(NotMatchCommentAuthorException.class)
    public ResponseEntity<StandardResponse> handleMismatchAuthorError(NotMatchCommentAuthorException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(StandardResponse.fail(e.getMessage(), e, HttpStatus.FORBIDDEN.value()));
    }
}
