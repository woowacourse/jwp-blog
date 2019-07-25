package techcourse.myblog.exception;

public class UnacceptablePathException extends RuntimeException {
    public UnacceptablePathException() {
        super("로그인이 필요합니다.");
    }

}
