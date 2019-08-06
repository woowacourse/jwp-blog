package techcourse.myblog.exception;

public class NoUserSessionException extends RuntimeException {
    public NoUserSessionException() {
        super("찾을 수 없는 댓글입니다.");
    }
}
