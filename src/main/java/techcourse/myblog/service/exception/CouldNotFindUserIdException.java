package techcourse.myblog.service.exception;

public class CouldNotFindUserIdException extends IllegalArgumentException {
    public CouldNotFindUserIdException() {
        super("유저 ID를 찾을 수 없습니다");
    }
}
