package techcourse.myblog.exception;

public class UserDuplicateException extends IllegalArgumentException {
    public UserDuplicateException(String s) {
        super(s);
    }
}
