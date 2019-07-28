package techcourse.myblog.service.exception;

public class IllegalRequestException extends CommentException {
    public IllegalRequestException(String message) {
        super(message);
    }
}
