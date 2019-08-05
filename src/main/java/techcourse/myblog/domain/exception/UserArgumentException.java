package techcourse.myblog.domain.exception;

public class UserArgumentException extends RuntimeException {
	public static final String EMAIL_DUPLICATION_MESSAGE = "Email duplication!";
	public static final String INVALID_NAME_LENGTH_MESSAGE = "Invalid name length!(2~10)";
	public static final String NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE =
			"Name can't include in special characters or Number!";
	public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "Invalid password length!(8~)";
	public static final String INVALID_PASSWORD_MESSAGE = "Invalid password!";
	public static final String PASSWORD_CONFIRM_FAIL_MESSAGE = "Password confirm fail!";

	public UserArgumentException(String message) {
		super(message);
	}
}
