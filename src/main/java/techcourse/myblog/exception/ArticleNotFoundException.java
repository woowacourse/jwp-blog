package techcourse.myblog.exception;

public class ArticleNotFoundException extends IllegalArgumentException {
    public ArticleNotFoundException(String s) {
        super(s);
    }
}
