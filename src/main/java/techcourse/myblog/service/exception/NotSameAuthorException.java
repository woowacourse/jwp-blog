package techcourse.myblog.service.exception;

public class NotSameAuthorException extends RuntimeException {
    public NotSameAuthorException(String message) {
        super(message);
    }
}
