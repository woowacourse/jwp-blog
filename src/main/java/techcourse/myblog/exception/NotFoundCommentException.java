package techcourse.myblog.exception;

public class NotFoundCommentException extends RuntimeException {
    private static final String message = "해당 덧글이 존재하지 않습니다.";

    public NotFoundCommentException() {
        super(message);
    }
}
