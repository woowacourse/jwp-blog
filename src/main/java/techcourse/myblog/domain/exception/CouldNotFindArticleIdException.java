package techcourse.myblog.domain.exception;

public class CouldNotFindArticleIdException extends RuntimeException {
    public CouldNotFindArticleIdException() {
        super("ID를 찾을 수 없습니다");
    }
}
