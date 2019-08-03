package techcourse.myblog.support.exception;

public class LoginFailedException extends RuntimeException {
    private static final String LOGIN_FAIL_MESSAGE = "이메일이나 비밀번호가 올바르지 않습니다";
    
    public LoginFailedException() {
        super(LOGIN_FAIL_MESSAGE);
    }
}
