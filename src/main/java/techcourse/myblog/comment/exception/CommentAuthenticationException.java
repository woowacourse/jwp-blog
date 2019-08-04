package techcourse.myblog.comment.exception;

public class CommentAuthenticationException extends AuthenticationException {
    public CommentAuthenticationException() {
        super("댓글에 권한이 없습니다.");
    }
}
