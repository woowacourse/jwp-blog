package techcourse.myblog.service.exception;

public class NotFoundArticleException extends RuntimeException {
	public static final String NOT_FOUND_ARTICLE_MESSAGE = "Not Found Article";

	public NotFoundArticleException() {
		super(NOT_FOUND_ARTICLE_MESSAGE);
	}
}
