package techcourse.myblog.exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String s) {
        super(s);
    }
}
