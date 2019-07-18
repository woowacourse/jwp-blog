package techcourse.myblog.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("아이디가 존재하지 않습니다.");
    }
}
