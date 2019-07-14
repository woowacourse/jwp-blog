package techcourse.myblog.domain;

public class ArticleNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "게시글이 존재하지 않습니다.";

    public ArticleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }
}
