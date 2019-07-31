package techcourse.myblog.exception;

public class SignUpException extends RuntimeException {
    public SignUpException() {
        super("회원가입에 실패했습니다.");
    }

    public SignUpException(String message) {
        super(message);
    }
}
