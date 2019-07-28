package techcourse.myblog.service.exception;

public class NotFoundCommentException extends CommentException {
    public NotFoundCommentException(String message) {
        super(message);
    }
}
