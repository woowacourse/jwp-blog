package techcourse.myblog.exception;

public abstract class UserLoginException extends RuntimeException {

    public UserLoginException(String message) {
        super(message);
    }

    public abstract String getName();
}
