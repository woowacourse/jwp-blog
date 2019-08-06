package techcourse.myblog.comment.exception;

public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException(long commentId) {
        super("[ " + commentId + " ]에 해당하는 댓글을 찾을 수 없습니다.");
    }
}
