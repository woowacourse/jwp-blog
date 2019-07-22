package techcourse.myblog.domain.exception;

import java.util.NoSuchElementException;

public class NotFoundUserException extends NoSuchElementException {

    public NotFoundUserException() {
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
