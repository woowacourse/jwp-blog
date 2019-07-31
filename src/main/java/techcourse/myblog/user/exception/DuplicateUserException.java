package techcourse.myblog.user.exception;

public class DuplicateUserException extends SignUpException {
    public DuplicateUserException() {
        super("이미 존재하는 이메일입니다.");
    }
}
