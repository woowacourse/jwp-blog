package techcourse.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ArticleDtoNotFoundException extends RuntimeException {
    public ArticleDtoNotFoundException() {
        super();
    }
}
