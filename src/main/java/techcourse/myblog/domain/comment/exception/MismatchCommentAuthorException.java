package techcourse.myblog.domain.comment.exception;

public class MismatchCommentAuthorException extends RuntimeException {
    private static final String MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE = "댓글 작성자가 아닙니다.";

    public MismatchCommentAuthorException() {
        super(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);
    }
}
