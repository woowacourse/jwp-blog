package techcourse.myblog.domain.article.exception;

public class IllegalUserException extends RuntimeException {
    public IllegalUserException(String msg) {
        super(msg);
    }
}
