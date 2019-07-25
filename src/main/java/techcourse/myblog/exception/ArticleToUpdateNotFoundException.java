package techcourse.myblog.exception;

public class ArticleToUpdateNotFoundException extends RuntimeException {
    public ArticleToUpdateNotFoundException(String message) {
        super(message);
    }
}
