package techcourse.myblog.exception;

public class UserException extends RuntimeException {

    private static final String NOT_FOUND_USER = "존재하지 않은 회원입니다.";

    public UserException() {
        super(NOT_FOUND_USER);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}