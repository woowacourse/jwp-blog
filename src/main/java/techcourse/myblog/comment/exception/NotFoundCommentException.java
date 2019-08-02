package techcourse.myblog.comment.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundCommentException extends RuntimeException {
    public NotFoundCommentException(long commentId) {
        super("찾을 수 없는 댓글입니다.");
        log.info("Requested CommentId: {}", commentId);
    }
}
