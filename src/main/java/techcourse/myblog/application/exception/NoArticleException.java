package techcourse.myblog.application.exception;

public class NoArticleException extends RuntimeException {
    public NoArticleException(String message) {
        super(message);
    }
}
