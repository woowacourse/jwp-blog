package techcourse.myblog.exception;

public class NoSuchUserException extends IllegalArgumentException {
    private static final String URL = "/user/login";

    public NoSuchUserException() {
        super("회원가입을 진행하세요");
    }

    public NoSuchUserException(String message) {
        super(message);
    }

    public String getUrl() {
        return URL;
    }
}
