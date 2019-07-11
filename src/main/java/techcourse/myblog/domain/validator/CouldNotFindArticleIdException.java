package techcourse.myblog.domain.validator;

public class CouldNotFindArticleIdException extends IllegalArgumentException {
    CouldNotFindArticleIdException() {
        super("ID를 찾을 수 없습니다");
    }
}
