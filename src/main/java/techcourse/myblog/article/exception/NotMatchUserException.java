package techcourse.myblog.article.exception;

public class NotMatchUserException extends RuntimeException {
    public NotMatchUserException() {
        super("로그인 정보와 일치하지 않습니다.");
    }
}
