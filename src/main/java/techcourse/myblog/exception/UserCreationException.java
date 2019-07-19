package techcourse.myblog.exception;

public class UserCreationException extends IllegalArgumentException {

	public UserCreationException() {
		super("User 클래스 생성 중 예외 발생");
	}

	public UserCreationException(final String message) {
		super(message);
	}
}
