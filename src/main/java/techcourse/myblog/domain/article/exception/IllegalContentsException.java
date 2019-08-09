package techcourse.myblog.domain.article.exception;

public class IllegalContentsException extends RuntimeException {
    public IllegalContentsException(String message) {
        super(message);
    }
}
