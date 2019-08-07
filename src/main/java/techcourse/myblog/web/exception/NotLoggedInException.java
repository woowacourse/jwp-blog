package techcourse.myblog.web.exception;

public class NotLoggedInException extends RuntimeException {
	public NotLoggedInException() {
		super("로그인되어있지 않습니다.");
	}
}
