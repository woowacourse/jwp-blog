package techcourse.myblog.exception;

public class NoArticleException extends RuntimeException {
    public NoArticleException() {
    }

    public NoArticleException(String message) {
        super(message);
    }

    public NoArticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoArticleException(Throwable cause) {
        super(cause);
    }

    public NoArticleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
