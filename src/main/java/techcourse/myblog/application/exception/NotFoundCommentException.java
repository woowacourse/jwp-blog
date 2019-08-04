package techcourse.myblog.application.exception;

public class NotFoundCommentException extends RuntimeException {
    private static final String NOT_FOUND_COMMENT_EXCEPTION_MESSAGE = "존재하지 않는 게시글입니다.";

    public NotFoundCommentException() {
        super(NOT_FOUND_COMMENT_EXCEPTION_MESSAGE);
    }
}
