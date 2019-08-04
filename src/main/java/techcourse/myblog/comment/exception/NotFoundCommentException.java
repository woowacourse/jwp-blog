package techcourse.myblog.comment.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
        super("댓글을 찾을 수 없습니다.");
    }

    public NotFoundCommentException(String message) {
        super(message);
    }
}