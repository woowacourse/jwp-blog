package techcourse.myblog.exception;

public class NotValidLoginException extends RuntimeException {
    public NotValidLoginException() {
    }

    public NotValidLoginException(final String message) {
        super(message);
    }

    public NotValidLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotValidLoginException(final Throwable cause) {
        super(cause);
    }

    public NotValidLoginException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
