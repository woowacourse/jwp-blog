package techcourse.myblog.domain;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException(String msg) {
        super(msg);
    }
}
