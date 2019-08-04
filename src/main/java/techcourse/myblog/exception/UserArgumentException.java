package techcourse.myblog.exception;

public class UserArgumentException extends RuntimeException {
    public UserArgumentException(String message) {
        super(message);
    }
}
