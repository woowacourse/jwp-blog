package techcourse.myblog.service.exception;

public class NoPermissionCommentException extends RuntimeException {
    public NoPermissionCommentException() {
        super();
    }

    public NoPermissionCommentException(String message) {
        super(message);
    }

    public NoPermissionCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionCommentException(Throwable cause) {
        super(cause);
    }

    protected NoPermissionCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
