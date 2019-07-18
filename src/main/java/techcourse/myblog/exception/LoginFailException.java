package techcourse.myblog.exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String s) {
        super(s);
    }
}
