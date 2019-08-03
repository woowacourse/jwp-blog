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
}
