package techcourse.myblog.web.controller;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class SignUpBindException extends BindException {
    public SignUpBindException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
