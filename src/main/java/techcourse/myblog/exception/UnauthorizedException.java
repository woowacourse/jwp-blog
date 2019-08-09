package techcourse.myblog.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("자신이 작성한 글만 수정/삭제가 가능합니다.");
    }
}
