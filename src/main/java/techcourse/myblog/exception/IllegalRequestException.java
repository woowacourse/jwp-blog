package techcourse.myblog.exception;

public class IllegalRequestException extends CommentException {
    public IllegalRequestException(String message) {
        super(message);
    }
}
