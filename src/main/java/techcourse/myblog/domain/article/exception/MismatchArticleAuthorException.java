package techcourse.myblog.domain.article.exception;

public class MismatchArticleAuthorException extends RuntimeException {
    private static final String MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE = "게시글 작성자가 아닙니다.";
    
    public MismatchArticleAuthorException() {
        super(MISMATCH_ARTICLE_AUTHOR_EXCEPTION_MESSAGE);
    }
}
