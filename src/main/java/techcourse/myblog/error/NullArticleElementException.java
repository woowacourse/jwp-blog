package techcourse.myblog.error;

public class NullArticleElementException extends RuntimeException {
    public NullArticleElementException(String msg) {
        super(msg);
    }
}
