package techcourse.myblog.application.service.exception;

public class DuplicatedIdException extends RuntimeException {
    public DuplicatedIdException(String message) {
        super(message);
    }
}