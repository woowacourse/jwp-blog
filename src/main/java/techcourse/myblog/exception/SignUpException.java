package techcourse.myblog.exception;

public class SignUpException extends RuntimeException {
    public SignUpException() {
    }

    public SignUpException(String message) {
        super(message);
    }

    public SignUpException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignUpException(Throwable cause) {
        super(cause);
    }

    public SignUpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
