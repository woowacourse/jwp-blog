package techcourse.myblog.exception;

public class NotFoundArticleException extends RuntimeException {
	public NotFoundArticleException() {
		super("해당 게시글을 찾을 수 없습니다.");
	}
}
