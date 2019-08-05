package techcourse.myblog.service.exception;

public class AlreadyExistEmailException extends RuntimeException {
	public AlreadyExistEmailException() {
		super("존재하는 이메일입니다.");
	}
}
