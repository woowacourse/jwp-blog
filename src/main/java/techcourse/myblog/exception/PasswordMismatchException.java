package techcourse.myblog.exception;

public class PasswordMismatchException extends IllegalArgumentException {
    private static final String URL = "/user/login";

    public PasswordMismatchException() {
        super("비밀번호를 제대로 입력하세요");
    }

    public PasswordMismatchException(String message) {
        super(message);
    }

    public String getUrl() {
        return URL;
    }
}
