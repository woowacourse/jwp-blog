package techcourse.myblog.service.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException(String message) {
        super(message);
    }
}
