package techcourse.myblog.domain.exception;

public class UserMismatchException extends RuntimeException {
    public static final String USER_MISMATCH_EXCEPTION = "User Mismatch";

    public UserMismatchException() {
        super("");
    }
}
