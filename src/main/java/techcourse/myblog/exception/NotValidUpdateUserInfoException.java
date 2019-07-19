package techcourse.myblog.exception;

import org.springframework.validation.FieldError;

import java.net.BindException;

public class NotValidUpdateUserInfoException extends BindException {
    public NotValidUpdateUserInfoException(FieldError fieldError) {
        super(fieldError.getDefaultMessage());
    }

}
