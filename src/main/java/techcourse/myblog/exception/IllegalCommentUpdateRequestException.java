package techcourse.myblog.exception;

public class IllegalCommentUpdateRequestException extends IllegalArgumentException {
    public IllegalCommentUpdateRequestException() {
    }

    public IllegalCommentUpdateRequestException(String s) {
        super(s);
    }
}
