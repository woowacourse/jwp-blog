package techcourse.myblog.user.exception;

import techcourse.myblog.user.domain.vo.Email;

public class DuplicatedUserException extends IllegalArgumentException {
    public DuplicatedUserException(Email email) {
        super("[ " + email.getEmail() + " ]은 중복된 이메일입니다.");
    }
}
