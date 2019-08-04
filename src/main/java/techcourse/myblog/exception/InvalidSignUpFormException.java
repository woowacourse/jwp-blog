package techcourse.myblog.exception;

public class InvalidSignUpFormException extends RuntimeException {
    public InvalidSignUpFormException(String message) {
        super(message);
    }
}
