package techcourse.myblog.service.exception;

public class SignUpFailedException extends RuntimeException {
    public SignUpFailedException(String msg) {
        super(msg);
    }
}
