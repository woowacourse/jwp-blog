package techcourse.myblog.user.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long userId) {
        super("[ " + userId + " ]에 해당하는 계정이 존재하지 않습니다.");
    }

    public NotFoundUserException(String email) {
        super("[ " + email + " ]에 해당하는 계정이 존재하지 않습니다.");
    }
}