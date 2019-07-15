package techcourse.myblog.domain;

public class NullArticleElementException extends RuntimeException {
    public NullArticleElementException(String msg) {
        super(msg);
    }
}
