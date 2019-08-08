package techcourse.myblog.domain.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String msg) {
        super(msg);
    }
}
