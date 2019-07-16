package techcourse.myblog.excerption;

public class ArticleToUpdateNotFoundException extends RuntimeException {
    public ArticleToUpdateNotFoundException(String message) {
        super(message);
    }
}
