package techcourse.myblog.article.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundArticleException extends IllegalArgumentException {
    public NotFoundArticleException(long articleId) {
        super("찾을 수 없는 게시물입니다.");
        log.info("Requested ArticleId: {}", articleId);
    }
}
