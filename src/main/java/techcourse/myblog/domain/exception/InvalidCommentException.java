package techcourse.myblog.domain.exception;

public class InvalidCommentException extends RuntimeException {
    public static final String INVALID_COMMENT_ERROR_MSG = "댓글은 비어있을 수 없습니다";

    public InvalidCommentException() {
        super(INVALID_COMMENT_ERROR_MSG);
    }
}
