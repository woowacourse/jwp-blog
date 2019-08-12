package techcourse.myblog.exception;

public class NotFoundUserException extends RuntimeException {
    public static final String message = "해당 유저를 찾을 수 없습니다.";

    public NotFoundUserException() {
        super(message);
    }
}
