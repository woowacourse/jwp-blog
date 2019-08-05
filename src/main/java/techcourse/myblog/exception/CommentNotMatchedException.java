package techcourse.myblog.exception;

public class CommentNotMatchedException extends IllegalArgumentException {
    public CommentNotMatchedException(String message) {
        super(message);
    }
}
