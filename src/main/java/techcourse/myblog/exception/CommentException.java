package techcourse.myblog.exception;

public class CommentException extends RuntimeException {

    private static final String NOT_FOUND_COMMENT = "댓글을 찾을 수 없습니다.";

    public CommentException() {
        super(NOT_FOUND_COMMENT);
    }

    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentException(Throwable cause) {
        super(cause);
    }

    protected CommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}