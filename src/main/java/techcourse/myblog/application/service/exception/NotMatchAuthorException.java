package techcourse.myblog.application.service.exception;

public class NotMatchAuthorException extends RuntimeException {
    public NotMatchAuthorException(String message) {
        super(message);
    }
}
