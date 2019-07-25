package techcourse.myblog.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException() {
        super("존재하지 않는 회원입니다.");
    }

    public NoSuchUserException(String message) {
        super(message);
    }
}
