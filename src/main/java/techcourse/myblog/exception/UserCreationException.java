package techcourse.myblog.exception;

public class UserCreationException extends IllegalArgumentException {
	private static final String SIGNUP_URL = "/user/signup";

	public UserCreationException() {
		super("User 클래스 생성 중 예외 발생");
	}

	public UserCreationException(final String message) {
		super(message);
	}

	public String getUrl() {
		return SIGNUP_URL;
	}
}
