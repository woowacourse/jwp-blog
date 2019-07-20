package techcourse.myblog.service.exception;

public class InvalidDataFormException extends IllegalArgumentException {
    public InvalidDataFormException(String message) {
        super(message);
    }
}
