package techcourse.myblog.service.exception;

public class NoArticleException extends IncludeRedirectUrlException {
    private static final String REDIRECT_URL = "errorpage";

    public NoArticleException(String message) {
        super(message, REDIRECT_URL);
    }
}
