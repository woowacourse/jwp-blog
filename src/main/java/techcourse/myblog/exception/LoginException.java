package techcourse.myblog.exception;

public class LoginException extends IllegalArgumentException {
	private static final String URL = "/user/login";

	public LoginException() {
	}

	public LoginException(final String message) {
		super(message);
	}

	public String getUrl() {
		return URL;
	}
}
