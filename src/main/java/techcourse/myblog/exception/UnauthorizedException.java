package techcourse.myblog.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("로그인 후 작성이 가능합니다.");
    }
}
