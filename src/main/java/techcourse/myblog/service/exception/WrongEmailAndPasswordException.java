package techcourse.myblog.service.exception;

public class WrongEmailAndPasswordException extends RuntimeException {

	public WrongEmailAndPasswordException() {
		super();
	}

	public WrongEmailAndPasswordException(String message) {
		super(message);
	}

	public WrongEmailAndPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongEmailAndPasswordException(Throwable cause) {
		super(cause);
	}

	protected WrongEmailAndPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
