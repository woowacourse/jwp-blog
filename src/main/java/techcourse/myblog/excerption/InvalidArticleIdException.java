package techcourse.myblog.excerption;

public class InvalidArticleIdException extends RuntimeException {
    public InvalidArticleIdException(String message) {
        super(message);
    }
}
