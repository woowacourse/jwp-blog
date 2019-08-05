package techcourse.myblog.service.exception;

public class NotFoundUserException extends RuntimeException {
	public NotFoundUserException() {
		super("해당 사용자를 찾을 수 없습니다.");
	}
}
