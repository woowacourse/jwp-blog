package techcourse.myblog.service;

public class SignUpException extends RuntimeException {
    public static final String EMAIL_DUPLICATION_MESSAGE = "Email duplication!";
    public static final String INVALID_NAME_LENGTH_MESSAGE = "Invalid name length!(2~10)";
    public static final String NAME_INCLUDE_INVALID_CHARACTERS_MESSAGE =
            "Name can't include in special characters or Number!";
    public static final String INVALID_PASSWORD_LENGTH_MESSAGE = "Invalid password length!(8~)";
    public static final String INVALID_PASSWORD_MESSAGE = "Invalid password!";

    public SignUpException(String errorMessage) {
        super(errorMessage);
    }
}
