package techcourse.myblog.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateUserException extends DuplicateKeyException {
    public DuplicateUserException() {
        super("이미 존재하는 이메일입니다.");
    }

    public DuplicateUserException(String msg) {
        super(msg);
    }
}
