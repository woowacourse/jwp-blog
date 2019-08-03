package techcourse.myblog.service;

public class SignUpFailedException extends RuntimeException {
    public SignUpFailedException(String msg) {
        super(msg);
    }
}
