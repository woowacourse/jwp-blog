package techcourse.myblog.application.service.exception;

public class NotMatchCommentAuthorException extends RuntimeException {
    public NotMatchCommentAuthorException(String message) {
        super(message);
    }
}