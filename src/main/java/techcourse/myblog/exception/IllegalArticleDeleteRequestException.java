package techcourse.myblog.exception;

public class IllegalArticleDeleteRequestException extends IllegalArgumentException {
    public IllegalArticleDeleteRequestException() {
    }

    public IllegalArticleDeleteRequestException(String s) {
        super(s);
    }
}
