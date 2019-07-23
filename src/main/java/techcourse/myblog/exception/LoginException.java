package techcourse.myblog.exception;

public class LoginException extends RuntimeException {
	public LoginException() {
		super("잘못된 아이디 또는 비밀번호입니다.");
	}
}
