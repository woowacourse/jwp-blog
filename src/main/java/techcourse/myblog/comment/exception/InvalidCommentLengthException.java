package techcourse.myblog.comment.exception;

public class InvalidCommentLengthException extends RuntimeException {
    public InvalidCommentLengthException() {
        super("200자를 초과했습니다.");
    }
}
