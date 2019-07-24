package techcourse.myblog.web.exception;

public class CouldNotRegisterException extends RuntimeException {
    public CouldNotRegisterException(String message) {
        super(message);
    }
}
