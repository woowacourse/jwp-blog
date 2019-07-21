package techcourse.myblog.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(final String message) {
        super(message);
    }
}
