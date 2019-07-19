package techcourse.myblog.exception;

import org.springframework.validation.FieldError;

import java.net.BindException;

public class NotValidUpdateUserInformation extends BindException {
    public NotValidUpdateUserInformation(FieldError fieldError) {
        super(fieldError.getDefaultMessage());
    }
}
