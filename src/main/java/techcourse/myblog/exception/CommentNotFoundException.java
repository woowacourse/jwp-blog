package techcourse.myblog.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("not found comment");
    }
}
