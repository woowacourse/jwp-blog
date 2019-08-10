package techcourse.myblog.application.exception;

public class NotSameAuthorException extends RuntimeException {
    public NotSameAuthorException(String message) {
        super(message);
    }
}
