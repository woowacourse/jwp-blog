package techcourse.myblog.web.exception;

public class LoginFailException extends IllegalArgumentException {
    public LoginFailException(String message) {
        super(message);
    }
}
