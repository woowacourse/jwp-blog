package techcourse.myblog.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("not found user");
    }
}
