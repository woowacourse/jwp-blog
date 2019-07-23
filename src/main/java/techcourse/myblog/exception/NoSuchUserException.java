package techcourse.myblog.exception;

public class NoSuchUserException extends LoginException {

	public NoSuchUserException() {
		super("회원가입을 진행하세요. 아이디가 없습니다.");
	}

	public NoSuchUserException(final String message) {
		super(message);
	}

	public String getUrl() {
		return super.getUrl();
	}
}
