package techcourse.myblog.support.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException(String msg) {
        super(msg);
    }
}
