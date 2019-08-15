package techcourse.myblog.service.exception;

public class NotFoundCommentException extends IncludeRedirectUrlException {
    private static final String REDIRECT_URL = "redirect:/";

    public NotFoundCommentException(String message) {
        super(message, REDIRECT_URL);
    }
}
