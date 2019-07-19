package techcourse.myblog.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateEmailException extends DuplicateKeyException {
    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.");
    }

    public DuplicateEmailException(String msg) {
        super(msg);
    }
}
