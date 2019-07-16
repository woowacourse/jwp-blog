package techcourse.myblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "저장할 게시글이 없습니다.")
public class ArticleToSaveNotFoundException extends RuntimeException {
    public ArticleToSaveNotFoundException(String message) {
        super(message);
    }
}
