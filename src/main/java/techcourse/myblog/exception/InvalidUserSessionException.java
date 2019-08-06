package techcourse.myblog.exception;

public class InvalidUserSessionException extends RuntimeException {
    public InvalidUserSessionException(String message) {
        super(message);
    }
}
