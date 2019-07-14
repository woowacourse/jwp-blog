package techcourse.myblog.domain;

public class InvalidArticleException extends RuntimeException {
    public InvalidArticleException(String msg) {
        super(msg);
    }
}
