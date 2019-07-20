package techcourse.myblog.web;

public class InvalidDataFormException extends IllegalArgumentException {
    public InvalidDataFormException(String message) {
        super(message);
    }
}
