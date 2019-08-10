package techcourse.myblog.web.exception;

public class InvalidUserSessionException extends RuntimeException {
    private static final String INVALID_USER_SESSION_ERROR_MSG = "로그인 세션이 유효하지 않습니다";

    public InvalidUserSessionException() {
        super(INVALID_USER_SESSION_ERROR_MSG);
    }
}
