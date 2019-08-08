package techcourse.myblog.web.exception;

public class UnauthorizedRequestException extends RuntimeException {
    public static final String UNAUTHORIZED_REQUEST_ERROR_MSG = "권한이 없는 요청입니다";

    public UnauthorizedRequestException() {
        super(UNAUTHORIZED_REQUEST_ERROR_MSG);
    }
}
