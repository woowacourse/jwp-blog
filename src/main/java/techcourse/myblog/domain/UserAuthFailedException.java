package techcourse.myblog.domain;

public class UserAuthFailedException extends RuntimeException {
    public UserAuthFailedException(String msg) {
        super(msg);
    }
}
