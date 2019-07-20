package techcourse.myblog.service.exception;

public class NotFindUserEmailException extends IllegalArgumentException {
    public NotFindUserEmailException() {
        super("존재하지 않는 이메일 입니다.");
    }
}
