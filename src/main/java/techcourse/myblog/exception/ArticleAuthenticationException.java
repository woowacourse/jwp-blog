package techcourse.myblog.exception;

public class ArticleAuthenticationException extends AuthenticationException {
    public ArticleAuthenticationException() {
        super("게시글에 권한이 없습니다.");
    }
}
