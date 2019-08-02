package techcourse.myblog.exception;

public class NotFoundArticleException extends RuntimeException {
    public static final String message = "게시글이 존재하지 않습니다.";

    public NotFoundArticleException() {
        super(message);
    }
}
