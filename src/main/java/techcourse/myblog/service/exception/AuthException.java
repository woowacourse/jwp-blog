package techcourse.myblog.service.exception;

public class AuthException extends RuntimeException {

    public AuthException(final String message) {
        super(message);
    }

    public AuthException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
