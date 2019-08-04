package techcourse.myblog.application.service.exception;

public class NotMatchEmailException extends RuntimeException {
    public NotMatchEmailException(String message) {
        super(message);
    }
}
