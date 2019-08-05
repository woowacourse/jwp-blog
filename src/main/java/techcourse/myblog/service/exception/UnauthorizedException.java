package techcourse.myblog.service.exception;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException() {
		super("권한이 없는 요청입니다.");
	}
}
