package techcourse.myblog.exception.valid;

public class InvalidSignUpFormException extends RuntimeException {
    public InvalidSignUpFormException(String message) {
        super(message);
    }
}
