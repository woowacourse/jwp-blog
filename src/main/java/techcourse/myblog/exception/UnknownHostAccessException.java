package techcourse.myblog.web;

public class UnknownHostAccessException extends RuntimeException {
    public UnknownHostAccessException() {
        super("로그인 후 이용해주세요.");
    }

    public UnknownHostAccessException(String message) {
        super(message);
    }
}
