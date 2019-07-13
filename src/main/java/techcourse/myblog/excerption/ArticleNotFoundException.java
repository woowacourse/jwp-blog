package techcourse.myblog.excerption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "존재하지 않는 게시글입니다.")
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super();
    }
}
