package techcourse.myblog.exception;

public class UserLoginInputException extends RuntimeException {
    public UserLoginInputException(final String message) {
        super(message);
    }
}
