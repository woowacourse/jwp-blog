package techcourse.myblog.service.exception;

public class NoCommentException extends IncludeRedirectUrlException {
    private static final String REDIRECT_URL = "redirect:/";

    public NoCommentException(String message) {
        super(message, REDIRECT_URL);
    }
}
