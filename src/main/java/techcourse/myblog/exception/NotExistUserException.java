package techcourse.myblog.exception;

public class NotExistUserException extends RuntimeException {
    public NotExistUserException(final String message) {
        super(message);
    }
}
