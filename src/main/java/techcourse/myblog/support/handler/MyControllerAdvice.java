package techcourse.myblog.support.handler;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.controller.LoginFailException;
import techcourse.myblog.service.DuplicateEmailException;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.RedirectAttributeSupport;

@ControllerAdvice
public class MyControllerAdvice {
    private UserDto userDto = new UserDto("", "", "");

    @ExceptionHandler(BindException.class)
    public RedirectView invalidData(BindException e, RedirectAttributes redirectAttributes) {
        RedirectAttributeSupport.addBindingResult(redirectAttributes, e.getBindingResult(), "userDto", userDto);
        return new RedirectView(e.getObjectName());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public RedirectView duplicateEmail(DuplicateEmailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler(LoginFailException.class)
    public RedirectView loginFail(LoginFailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new RedirectView("/login");
    }


}
