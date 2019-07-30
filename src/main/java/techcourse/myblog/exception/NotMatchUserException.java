package techcourse.myblog.exception;

public class NotMatchUserException extends RuntimeException {
    public NotMatchUserException() {
        super("본인이 아닙니다.");
    }

}
