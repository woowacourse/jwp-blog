package techcourse.myblog.service.exception;

public class LogInException extends RuntimeException {
    public static final String LOGIN_FAIL_MESSAGE = "Fail login";

    public LogInException(String message) {
        super(message);
    }
}
