package techcourse.myblog.presentation.argument.exception;

public class NotSignedInException extends RuntimeException {
    private static final String NOT_SIGNED_IN_EXCEPTION_MESSAGE = "로그인하지 않았습니다.";

    public NotSignedInException() {
        super(NOT_SIGNED_IN_EXCEPTION_MESSAGE);
    }
}
