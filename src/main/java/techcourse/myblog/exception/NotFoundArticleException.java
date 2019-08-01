package techcourse.myblog.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException(String message) {
        super(message);
    }
}
