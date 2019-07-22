package techcourse.myblog.service;

public class UnfoundUserException extends RuntimeException {
    public UnfoundUserException(String msg) {
        super(msg);
    }
}
