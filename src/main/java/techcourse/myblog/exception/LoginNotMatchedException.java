package techcourse.myblog.exception;

public class LoginNotMatchedException extends RuntimeException {
    public LoginNotMatchedException() {
        super("아이디나 비밀번호가 잘못되었습니다.");
    }
}
