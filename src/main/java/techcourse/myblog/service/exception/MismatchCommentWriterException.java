package techcourse.myblog.service.exception;

public class MismatchCommentWriterException extends RuntimeException {

    public static final String MISMATCH_WRITER_ERROR_MESSAGE = "댓글 작성자가 아닙니다.";

    public MismatchCommentWriterException() {
        super(MISMATCH_WRITER_ERROR_MESSAGE);
    }
}
