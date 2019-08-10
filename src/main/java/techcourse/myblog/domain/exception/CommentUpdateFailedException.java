package techcourse.myblog.domain.exception;

public class CommentUpdateFailedException extends RuntimeException {
    public static final String COMMENT_UPDATE_FAIL_ERROR_MSG = "댓글을 수정할 수 없습니다";

    public CommentUpdateFailedException() {
        super(COMMENT_UPDATE_FAIL_ERROR_MSG);
    }
}
