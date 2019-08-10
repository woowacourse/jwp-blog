package techcourse.myblog.application.exception;

public class EmptyCommentRequestException extends RuntimeException {
    private static final String MESSAGE = "댓글 내용을 입력하세요.";

    public EmptyCommentRequestException() {
        super(MESSAGE);
    }
}
