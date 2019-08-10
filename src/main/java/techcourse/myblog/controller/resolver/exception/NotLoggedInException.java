package techcourse.myblog.controller.resolver.exception;

public class NotLoggedInException extends RuntimeException {
    public static final String NOT_LOGGED_IN_MESSAGE = "로그인되어있지 않습니다.";

    public NotLoggedInException() {
        super(NOT_LOGGED_IN_MESSAGE);
    }
}
