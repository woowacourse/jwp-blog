package techcourse.myblog.application.service.exception;

public class NotExistArticleIdException extends RuntimeException {
    public NotExistArticleIdException(String message) {
        super(message);
    }
}