package techcourse.myblog.exception;

public class LoginException extends RuntimeException {
    private LoginException(String message) {
        super(message);
    }

    public static LoginException notFoundEmail() {
        return new LoginException("이메일이 존재하지 않습니다.");
    }

    public static LoginException notMatchPassword() {
        return new LoginException("패스워드가 일치하지 않습니다.");
    }

    public static LoginException alreadyLogin() {
        return new LoginException("이미 로그인이 되었습니다.");
    }
}
