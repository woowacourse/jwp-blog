package techcourse.myblog.exception;

public class NotFoundArticleException extends IllegalArgumentException {
    public NotFoundArticleException() {
        super("찾을 수 없는 게시물입니다.");
    }
}
