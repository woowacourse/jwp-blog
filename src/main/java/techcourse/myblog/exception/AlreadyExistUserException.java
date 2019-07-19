package techcourse.myblog.exception;

public class AlreadyExistUserException extends RuntimeException {
    public AlreadyExistUserException(final String message) {
        super(message);
    }
}
