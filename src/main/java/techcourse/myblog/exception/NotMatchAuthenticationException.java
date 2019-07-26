package techcourse.myblog.exception;

public class NotMatchAuthenticationException extends RuntimeException {
    public NotMatchAuthenticationException(final String message) {
        super(message);
    }
}
