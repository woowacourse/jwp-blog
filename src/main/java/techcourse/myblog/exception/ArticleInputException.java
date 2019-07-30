package techcourse.myblog.exception;

public class ArticleInputException extends RuntimeException {
    public ArticleInputException(final String message) {
        super(message);
    }
}
