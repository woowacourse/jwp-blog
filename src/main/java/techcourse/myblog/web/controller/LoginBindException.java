package techcourse.myblog.web.controller;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class LoginBindException extends BindException {
    public LoginBindException(BindingResult bindingResult) {
        super(bindingResult);
    }
}
