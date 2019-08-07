package techcourse.myblog.service.exception;

public class NotFoundUserException extends RuntimeException {
	public static final String NOT_FOUND_USER_MESSAGE = "Not Found User";

	public NotFoundUserException() {
		super(NOT_FOUND_USER_MESSAGE);
	}
}
