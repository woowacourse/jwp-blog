package techcourse.myblog.exception;

public class NotMatchAuthorException extends RuntimeException {
    public NotMatchAuthorException() {
        super("작성자가 아닙니다.");
    }
}
