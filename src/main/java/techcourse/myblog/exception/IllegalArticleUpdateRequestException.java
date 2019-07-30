package techcourse.myblog.exception;

public class IllegalArticleUpdateRequestException extends IllegalArgumentException {
    public IllegalArticleUpdateRequestException() {
    }

    public IllegalArticleUpdateRequestException(String s) {
        super(s);
    }
}
