package techcourse.myblog.domain.exception;

public class IllegalIdException extends RuntimeException {
    public IllegalIdException(String message) {
        super(message);
    }
}
