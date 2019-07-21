package techcourse.myblog.application.service.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String message) {
        super(message);
    }
}