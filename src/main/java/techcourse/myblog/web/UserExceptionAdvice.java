package techcourse.myblog.web;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.exception.DuplicatedUserException;

@ControllerAdvice
public class UserExceptionAdvice {
    @ExceptionHandler(BindException.class)
    public RedirectView handelBindingException(BindException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                e.getFieldError().getDefaultMessage());
        return new RedirectView("/signup");
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public RedirectView handelDuplicatedUserException(DuplicatedUserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                e.getMessage());
        return new RedirectView("/signup");
    }
}
