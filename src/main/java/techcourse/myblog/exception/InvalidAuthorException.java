package techcourse.myblog.exception;

public class InvalidAuthorException extends RuntimeException {
	public InvalidAuthorException() {
		super("자신이 작성한 글만 수정/삭제가 가능합니다.");
	}
}
