package techcourse.myblog.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("존재하지 않은 회원입니다.");
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}