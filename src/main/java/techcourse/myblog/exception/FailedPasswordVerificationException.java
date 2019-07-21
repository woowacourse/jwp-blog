package techcourse.myblog.exception;

public class FailedPasswordVerificationException extends IllegalArgumentException {
    public FailedPasswordVerificationException() {
        super("비밀번호를 다시 확인해 주세요.");
    }

    public FailedPasswordVerificationException(String msg) {
        super(msg);
    }
}
