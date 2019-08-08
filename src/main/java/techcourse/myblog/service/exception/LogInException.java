package techcourse.myblog.service.exception;

public class LogInException extends RuntimeException {
    public static final String NOT_FOUND_USER_MESSAGE = "Not Found User!";
    public static final String PASSWORD_FAIL_MESSAGE = "Password Mismatch!";

    public LogInException(String message) {
        super(message);
    }
}
