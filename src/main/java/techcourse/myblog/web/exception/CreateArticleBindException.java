package techcourse.myblog.web.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class CreateArticleBindException extends BindException {
    public CreateArticleBindException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
