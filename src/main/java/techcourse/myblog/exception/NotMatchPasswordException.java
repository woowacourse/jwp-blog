package techcourse.myblog.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(final String message) {
        super(message);
    }
}
