package techcourse.myblog.exception;

public class NotFoundCommentException extends CommentException {
    public NotFoundCommentException(String message) {
        super(message);
    }
}
