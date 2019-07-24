package techcourse.myblog.exception;

public class UserDuplicateException extends IllegalArgumentException {
    public UserDuplicateException() {
    }

    public UserDuplicateException(String s) {
        super(s);
    }
}
