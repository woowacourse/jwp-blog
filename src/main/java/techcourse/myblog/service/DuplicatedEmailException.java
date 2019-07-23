package techcourse.myblog.service;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
