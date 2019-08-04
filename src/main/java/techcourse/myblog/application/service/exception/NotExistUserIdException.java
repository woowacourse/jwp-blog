package techcourse.myblog.application.service.exception;

public class NotExistUserIdException extends RuntimeException {
    public NotExistUserIdException(String message) {
        super(message);
    }
}
