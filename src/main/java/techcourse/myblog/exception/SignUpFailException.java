package techcourse.myblog.exception;

public class SignUpFailException extends RuntimeException {
    public SignUpFailException() {
    }

    public SignUpFailException(String message) {
        super(message);
    }

    public SignUpFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignUpFailException(Throwable cause) {
        super(cause);
    }

    public SignUpFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
