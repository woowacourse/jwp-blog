package techcourse.myblog.domain.validator;

public class DuplicateArticleIdException extends IllegalArgumentException {
    public DuplicateArticleIdException() {
        super("중복된 ID를 가진 게시물이 있습니다.");
    }
}
