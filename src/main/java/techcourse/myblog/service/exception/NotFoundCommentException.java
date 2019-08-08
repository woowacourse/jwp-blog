package techcourse.myblog.service.exception;

public class NotFoundCommentException extends RuntimeException {
    public static final String NOT_FOUND_COMMENT_ERROR_MSG = "존재하지 않는 댓글입니다";

    public NotFoundCommentException() {
        super(NOT_FOUND_COMMENT_ERROR_MSG);
    }
}
