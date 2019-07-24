package techcourse.myblog.exception;

public class UserMismatchException extends IllegalArgumentException {
    public UserMismatchException() {
    }

    public UserMismatchException(String s) {
        super(s);
    }
}
