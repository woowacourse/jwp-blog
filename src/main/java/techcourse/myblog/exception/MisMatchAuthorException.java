package techcourse.myblog.exception;

public class MisMatchAuthorException extends RuntimeException {
    public MisMatchAuthorException(String message) {
        super(message);
    }
}
