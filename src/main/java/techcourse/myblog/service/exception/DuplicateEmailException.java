package techcourse.myblog.service.exception;

public class DuplicateEmailException extends IllegalArgumentException {
    public DuplicateEmailException() {
        super("해당 이메일은 존재하는 이메일 입니다.");
    }
}
