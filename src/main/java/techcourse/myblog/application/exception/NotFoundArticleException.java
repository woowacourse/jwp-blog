package techcourse.myblog.application.exception;

public class NotFoundArticleException extends NotFoundException {
    private static final String NOT_FOUND_ARTICLE_EXCEPTION_MESSAGE = "존재하지 않는 게시글입니다.";

    public NotFoundArticleException() {
        super(NOT_FOUND_ARTICLE_EXCEPTION_MESSAGE);
    }
}
