package techcourse.myblog.support.exception;

public class IllegalUserException extends RuntimeException {
    public IllegalUserException(String msg) {
        super(msg);
    }
}
