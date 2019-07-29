package techcourse.myblog.exception;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException(String message) {
        super(message);
    }
}
