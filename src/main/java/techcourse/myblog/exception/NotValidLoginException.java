package techcourse.myblog.exception;

public class NotValidLoginException extends RuntimeException {
    public NotValidLoginException(final String message) {
        super(message);
    }
}
