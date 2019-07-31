package techcourse.myblog.support.exception;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException(String msg) {
        super(msg);
    }
}
