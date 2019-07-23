package techcourse.myblog.exception;

public class LoginException extends RuntimeException {
	public LoginException() {
		super("로그인 처리 중 오류가 발생했습니다.");
	}

	public LoginException(String message) {
		super(message);
	}
}
