package techcourse.myblog.web.exception;

public class UserArticleMissmatchException extends RuntimeException {
    public UserArticleMissmatchException() {
        super("글에 대한 권한이 없습니다.");
    }
}
