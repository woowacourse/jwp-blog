package techcourse.myblog.exception;

public class NotFoundObjectException extends RuntimeException {
	public NotFoundObjectException() {
		super("객체가 repository 안에 존재하지 않습니다.");
	}
}
