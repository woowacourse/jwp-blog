package techcourse.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "적절한 ID가 아닙니다.")
public class InvalidArticleIdException extends RuntimeException {
    public InvalidArticleIdException(String message) {
        super(message);
    }
}
