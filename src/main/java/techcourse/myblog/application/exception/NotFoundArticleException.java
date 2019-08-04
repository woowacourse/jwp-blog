package techcourse.myblog.application.exception;

public class NotFoundArticleException extends RuntimeException {
    private static final String NOT_FOUND_ARTICLE_ERROR_MSG = "존재하지 않는 게시글입니다.";

    public NotFoundArticleException() {
        super(NOT_FOUND_ARTICLE_ERROR_MSG);
    }
}
