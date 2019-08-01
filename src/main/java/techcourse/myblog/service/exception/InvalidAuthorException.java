package techcourse.myblog.service.exception;

public class InvalidAuthorException extends IncludeRedirectUrlException {
    private static final String REDIRECT_URL = "redirect:/";

    public InvalidAuthorException(String message) {
        super(message, REDIRECT_URL);
    }
}
