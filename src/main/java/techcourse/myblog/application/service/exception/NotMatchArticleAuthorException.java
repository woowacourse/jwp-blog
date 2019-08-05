package techcourse.myblog.application.service.exception;

public class NotMatchArticleAuthorException extends RuntimeException {
    public NotMatchArticleAuthorException(String message) {
        super(message);
    }
}
