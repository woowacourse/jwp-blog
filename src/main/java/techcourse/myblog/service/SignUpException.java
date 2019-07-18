package techcourse.myblog.service;

public class SignUpException extends RuntimeException {
    public static final String EMAIL_DUPLICATION_MESSAGE = "email duplication!";

    public SignUpException(String errorMessage) {
        super(errorMessage);
    }
}
