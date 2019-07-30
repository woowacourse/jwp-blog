package techcourse.myblog.exception;

public class CommentNotFoundException extends IllegalArgumentException {
    public CommentNotFoundException() {
    }

    public CommentNotFoundException(String s) {
        super(s);
    }
}
