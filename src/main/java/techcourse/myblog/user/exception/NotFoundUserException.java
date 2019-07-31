package techcourse.myblog.user.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long userId) {
        super("아이디가 존재하지 않습니다.");
        log.info("Requested User Id: {}", userId);
    }

    public NotFoundUserException(String email) {
        super("해당 이메일은 존재하지 않습니다.");
        log.info("Requested User Email: {}", email);
    }
}
