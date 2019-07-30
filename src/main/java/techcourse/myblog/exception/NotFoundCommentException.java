package techcourse.myblog.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException() {
        super("찾을 수 없는 댓글입니다.");
    }
}
