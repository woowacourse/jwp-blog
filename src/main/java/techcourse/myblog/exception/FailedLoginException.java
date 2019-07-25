package techcourse.myblog.exception;

import java.util.NoSuchElementException;

public class FailedLoginException extends NoSuchElementException {
    public FailedLoginException() {
        super("로그인에 실패했습니다.");
    }

    public FailedLoginException(String message) {
        super(message);
    }
}
