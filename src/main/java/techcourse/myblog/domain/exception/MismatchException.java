package techcourse.myblog.domain.exception;

public class MismatchException extends RuntimeException {
    public MismatchException(String message) {
        super(message);
    }
}
