package techcourse.myblog.exception;

public class UnequalPasswordException extends IllegalArgumentException {
    public UnequalPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
