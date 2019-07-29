package techcourse.myblog.exception;

public class NotFoundCommentException extends  RuntimeException {
	public NotFoundCommentException() {
		super("존재하지 않는 댓글입니다.");
	}
}
