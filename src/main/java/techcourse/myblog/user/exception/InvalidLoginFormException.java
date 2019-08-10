package techcourse.myblog.user.exception;

public class InvalidLoginFormException extends RuntimeException {
    public InvalidLoginFormException(String message) {
        super(message);
    }
}
