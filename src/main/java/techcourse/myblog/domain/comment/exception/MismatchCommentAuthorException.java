package techcourse.myblog.domain.comment.exception;

import techcourse.myblog.domain.exception.MismatchException;

public class MismatchCommentAuthorException extends MismatchException {
    private static final String MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE = "댓글 작성자가 아닙니다.";

    public MismatchCommentAuthorException() {
        super(MISMATCH_COMMENT_AUTHOR_EXCEPTION_MESSAGE);
    }
}
