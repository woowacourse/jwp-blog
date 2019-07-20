package techcourse.myblog.exception;

public class InvalidEditFormException extends RuntimeException {
    public InvalidEditFormException(String message) {
        super(message);
    }
}
