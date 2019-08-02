package techcourse.myblog.service.exception;

public class NotFoundCommentException extends RuntimeException {
    public static final String NOT_FOUND_COMMENT_MESSAGE = "Not Found Comment";

    public NotFoundCommentException() {
        super(NOT_FOUND_COMMENT_MESSAGE);
    }
}
