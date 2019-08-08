package techcourse.myblog.service.exception;

public class NotValidUserInfoException extends RuntimeException {
    public NotValidUserInfoException(String message) {
        super(message);
    }
}
