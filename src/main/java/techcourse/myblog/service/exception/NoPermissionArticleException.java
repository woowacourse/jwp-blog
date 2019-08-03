package techcourse.myblog.service.exception;

public class NoPermissionArticleException extends RuntimeException {
    public NoPermissionArticleException() {
        super();
    }

    public NoPermissionArticleException(String message) {
        super(message);
    }

    public NoPermissionArticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionArticleException(Throwable cause) {
        super(cause);
    }

    protected NoPermissionArticleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
