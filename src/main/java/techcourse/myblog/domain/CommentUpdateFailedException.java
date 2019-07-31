package techcourse.myblog.domain;

public class CommentUpdateFailedException extends RuntimeException {
    public CommentUpdateFailedException(String msg) {
        super(msg);
    }
}
