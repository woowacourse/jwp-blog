package techcourse.myblog.support.exception;

public class MismatchAuthorException extends RuntimeException {
    private static final String MISMATCH_AUTHOR_ERROR_MSG = "작성자가 아닙니다.";
    
    public MismatchAuthorException() {
        super(MISMATCH_AUTHOR_ERROR_MSG);
    }
}
