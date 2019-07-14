package techcourse.myblog.domain;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String msg) {
        super(msg);
    }
}
