package techcourse.myblog.domain.comment.exception;

public class InvalidCommentException extends RuntimeException {
    private static final String COMMENT_EMPTY_ERROR_MSG = "댓글은 비어있을 수 없습니다.";
    
    public InvalidCommentException() {
        super(COMMENT_EMPTY_ERROR_MSG);
    }
}
