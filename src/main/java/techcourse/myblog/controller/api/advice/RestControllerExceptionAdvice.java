package techcourse.myblog.controller.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.controller.resolver.exception.NotLoggedInException;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.NotFoundCommentException;

@RestControllerAdvice(basePackages = "techcourse.myblog.controller.api")
public class RestControllerExceptionAdvice {
    @ExceptionHandler({NotFoundArticleException.class, NotFoundCommentException.class, UserMismatchException.class})
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<ErrorMessage> handleLogInException(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
