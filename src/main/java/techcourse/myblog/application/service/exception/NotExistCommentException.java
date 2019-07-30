package techcourse.myblog.application.service.exception;

public class NotExistCommentException extends RuntimeException {
    public NotExistCommentException(String message) {
        super(message);
    }
}

