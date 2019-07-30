package techcourse.myblog.user.exception;

public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException() {
        super("본인의 아이디가 아닙니다.");
    }
}