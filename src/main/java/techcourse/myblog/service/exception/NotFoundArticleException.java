package techcourse.myblog.service.exception;

public class NotFoundArticleException extends RuntimeException {
	public NotFoundArticleException() {
		super("존재하지 않는 게시글입니다.");
	}
}
