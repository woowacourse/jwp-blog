package techcourse.myblog.service.exception;

public class UnFoundArticleException extends RuntimeException {
    public UnFoundArticleException(String message) {
        super(message);
    }
}
