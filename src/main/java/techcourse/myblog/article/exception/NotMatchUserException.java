package techcourse.myblog.article.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotMatchUserException extends RuntimeException {
    public NotMatchUserException(long authorId) {
        super("로그인 정보와 일치하지 않습니다.");
        log.info("Requested AuthorId: {}", authorId);
    }
}
