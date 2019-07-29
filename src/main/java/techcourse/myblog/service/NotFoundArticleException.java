package techcourse.myblog.service;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException(String msg) {
        super(msg);
    }
}
