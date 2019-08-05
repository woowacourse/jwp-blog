package techcourse.myblog.exception;

public class UserDuplicateEmailException extends RuntimeException {
    public UserDuplicateEmailException() {
        super("이메일 중복입니다.");
    }
}
