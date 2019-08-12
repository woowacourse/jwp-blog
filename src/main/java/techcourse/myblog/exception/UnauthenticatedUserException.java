package techcourse.myblog.exception;

public class UnauthenticatedUserException extends RuntimeException {
    public static final String message = "권한이 없습니다.";

    public UnauthenticatedUserException() {
        super(message);
    }
}
