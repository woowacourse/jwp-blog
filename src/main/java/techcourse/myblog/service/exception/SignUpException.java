package techcourse.myblog.service.exception;

public class SignUpException extends IncludeRedirectUrlException {
    private static final String REDIRECT_URL = "signup";

    public SignUpException(String message) {
        super(message, REDIRECT_URL);
    }
}
