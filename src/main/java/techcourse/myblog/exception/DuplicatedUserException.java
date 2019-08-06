package techcourse.myblog.exception;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException() {
        super("중복된 e-mail입니다.");
    }
}
