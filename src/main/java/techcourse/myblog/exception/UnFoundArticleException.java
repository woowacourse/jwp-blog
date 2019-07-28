package techcourse.myblog.exception;

public class UnFoundArticleException extends RuntimeException {
    public UnFoundArticleException(String message) {
        super(message);
    }
}
