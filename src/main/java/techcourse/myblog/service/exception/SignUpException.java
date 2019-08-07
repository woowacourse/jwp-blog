package techcourse.myblog.service.exception;

public class SignUpException extends RuntimeException {
    public static final String SIGN_UP_FAIL_MESSAGE = "Sign Up fail";

    public SignUpException(final String errorMessage) {
	super(SIGN_UP_FAIL_MESSAGE + " : " + errorMessage);
    }
}
