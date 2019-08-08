package techcourse.myblog.service.exception;

public class NotFoundUserException extends RuntimeException {
    public static final String UNFOUND_USER_ERROR_MSG = "존재하지 않는 사용자입니다";

    public NotFoundUserException() {
        super(UNFOUND_USER_ERROR_MSG);
    }
}
