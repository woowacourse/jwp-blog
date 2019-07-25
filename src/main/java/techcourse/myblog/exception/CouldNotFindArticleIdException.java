package techcourse.myblog.exception;

public class CouldNotFindArticleIdException extends IllegalArgumentException {
    public CouldNotFindArticleIdException() {
        super("게시글 ID를 찾을 수 없습니다");
    }
}
