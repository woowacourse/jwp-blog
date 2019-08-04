package techcourse.myblog.exception;

public class IllegalCommentDeleteRequestException extends IllegalArgumentException {
    public IllegalCommentDeleteRequestException() {
    }

    public IllegalCommentDeleteRequestException(String s) {
        super(s);
    }
}
