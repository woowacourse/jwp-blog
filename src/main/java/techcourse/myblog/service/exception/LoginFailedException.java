package techcourse.myblog.service.exception;

public class LoginFailedException extends RuntimeException {
    public static final String LOGIN_FAIL_MESSAGE = "이메일이나 비밀번호가 올바르지 않습니다";

    public LoginFailedException() {
        super(LOGIN_FAIL_MESSAGE);
    }
}
