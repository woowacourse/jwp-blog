package techcourse.myblog.exception;

public class UserUpdateFailException extends RuntimeException {
    public UserUpdateFailException(String message) {
        super(message);
    }
}
