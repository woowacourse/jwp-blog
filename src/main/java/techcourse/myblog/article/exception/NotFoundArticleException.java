package techcourse.myblog.article.exception;

public class NotFoundArticleException extends RuntimeException {
    public NotFoundArticleException(long articleId) {
        super("[ " + articleId + "]에 해당하는 게시물을 찾을 수 없습니다.");
    }
}
