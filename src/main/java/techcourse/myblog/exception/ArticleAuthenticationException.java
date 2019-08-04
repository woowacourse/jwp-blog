package techcourse.myblog.exception;

public class ArticleAuthenticationException extends RuntimeException {

    private static final String ACCESS_AUTHENTICATION_ERROR = "게시글에 대한 권한이 없습니다.";

    public ArticleAuthenticationException() {
        super(ACCESS_AUTHENTICATION_ERROR);
    }

    public ArticleAuthenticationException(String message) {
        super(message);
    }

    public ArticleAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleAuthenticationException(Throwable cause) {
        super(cause);
    }

    protected ArticleAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
