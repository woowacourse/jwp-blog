package techcourse.myblog.service.exception;

public class NotFoundArticleException extends IncludeRedirectUrlException {
    public static final String NO_ARTICLE_MSG = "게시글이 존재하지 않습니다";

    private static final String REDIRECT_URL = "errorpage";

    public NotFoundArticleException(String message) {
        super(message, REDIRECT_URL);
    }
}
