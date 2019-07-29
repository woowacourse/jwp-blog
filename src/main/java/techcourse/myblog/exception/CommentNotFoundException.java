package techcourse.myblog.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(final String message) {
        super(message);
    }
}
