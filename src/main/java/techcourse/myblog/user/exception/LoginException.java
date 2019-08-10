package techcourse.myblog.user.exception;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("로그인에 실패했습니다.");
    }

    public LoginException(String message) {
        super(message);
    }
}
