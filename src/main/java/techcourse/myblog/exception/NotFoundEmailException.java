package techcourse.myblog.exception;

public class NotFoundEmailException extends LoginException {
	public NotFoundEmailException() {
		super("존재하지 않는 아이디입니다.");
	}
}
