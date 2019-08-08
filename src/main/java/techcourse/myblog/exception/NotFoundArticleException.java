package techcourse.myblog.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException() {
        super("해당 게시글이 존재하지 않습니다.");
    }
}
