package techcourse.myblog.exception;

public class ArticleException extends RuntimeException {

    private static final String NOT_FOUND_ARTICLE = "해당 기사가 없습니다.";

    public ArticleException() {
        super(NOT_FOUND_ARTICLE);
    }

    public ArticleException(String message) {
        super(message);
    }

    public ArticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleException(Throwable cause) {
        super(cause);
    }

    protected ArticleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}