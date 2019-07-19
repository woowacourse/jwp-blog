package techcourse.myblog.web;

public class InvalidSighUpException extends RuntimeException {
    public InvalidSighUpException(String message) {
        super(message);
    }
}
