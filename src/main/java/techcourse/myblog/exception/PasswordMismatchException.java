package techcourse.myblog.exception;

public class PasswordMismatchException extends LoginException {

    public PasswordMismatchException() {
        super("올바른 비밀번호를 입력하세요");
    }

    public PasswordMismatchException(final String message) {
        super(message);
    }

    public String getUrl() {
        return super.getUrl();
    }
}
