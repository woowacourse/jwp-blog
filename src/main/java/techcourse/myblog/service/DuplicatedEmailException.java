package techcourse.myblog.service;

public class DuplicatedEmailException extends RuntimeException {
    public DuplicatedEmailException(String msg) {
        super(msg);
    }
}
