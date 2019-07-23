package techcourse.myblog.exception;

public class NameToUpdateNotFoundException extends RuntimeException {
    public NameToUpdateNotFoundException(String message) {
        super(message);
    }
}
