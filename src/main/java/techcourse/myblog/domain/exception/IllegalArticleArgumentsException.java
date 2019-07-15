package techcourse.myblog.domain.exception;

public class IllegalArticleArgumentsException extends IllegalArgumentException {
    public IllegalArticleArgumentsException() {
        super("Article title, coverUrl, contents에는 null값이 들어갈 수 없습니다.");
    }
}
