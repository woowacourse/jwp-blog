package techcourse.myblog.comment.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("권한이 없습니다.");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
