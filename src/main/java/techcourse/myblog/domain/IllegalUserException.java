package techcourse.myblog.domain;

public class IllegalUserException extends RuntimeException {
    public IllegalUserException(String msg) {
        super(msg);
    }
}
