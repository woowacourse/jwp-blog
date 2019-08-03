package techcourse.myblog.service.exception;

public class NoRowException extends RuntimeException {
    public NoRowException() {
        super();
    }

    public NoRowException(String message) {
        super(message);
    }

    public NoRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRowException(Throwable cause) {
        super(cause);
    }

    protected NoRowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
