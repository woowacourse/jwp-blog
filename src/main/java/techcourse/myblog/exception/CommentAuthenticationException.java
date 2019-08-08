package techcourse.myblog.exception;

import techcourse.myblog.exception.exception.AuthenticationException;

public class CommentAuthenticationException extends AuthenticationException {
    public CommentAuthenticationException() {
        super("댓글에 권한이 없습니다.");
    }
}
