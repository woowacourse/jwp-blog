package techcourse.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "업데이트 해야할 게시글이 없습니다.")
public class ArticleToUpdateNotFoundException extends RuntimeException {
    public ArticleToUpdateNotFoundException(String message) {
        super(message);
    }
}
