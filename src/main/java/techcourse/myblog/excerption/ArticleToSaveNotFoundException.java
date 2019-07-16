package techcourse.myblog.excerption;

public class ArticleToSaveNotFoundException extends RuntimeException {
    public ArticleToSaveNotFoundException(String message) {
        super(message);
    }
}
