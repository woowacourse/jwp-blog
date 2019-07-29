package techcourse.myblog.service;

public class MismatchAuthorException extends RuntimeException {
    public MismatchAuthorException(String msg) {
        super(msg);
    }
}
