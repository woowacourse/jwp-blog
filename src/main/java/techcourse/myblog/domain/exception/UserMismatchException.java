package techcourse.myblog.domain.exception;

public class UserMismatchException extends RuntimeException {
    public static final String USER_MISMATCH_MESSAGE = "User Mismatch";

    public UserMismatchException() {
        super(USER_MISMATCH_MESSAGE);
    }
}
