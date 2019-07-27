package techcourse.myblog.controller.core;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.service.exception.DuplicatedUserException;
import techcourse.myblog.service.exception.NotMatchPasswordException;
import techcourse.myblog.service.exception.UnFoundUserException;
import techcourse.myblog.service.exception.UserLoginException;

@Slf4j
@ControllerAdvice
public class UserLoginExceptionAdvice {
    @ExceptionHandler({UnFoundUserException.class, NotMatchPasswordException.class})
    public RedirectView unFoundExceptionHandler(UserLoginException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/login");
    }

    @ExceptionHandler({DuplicatedUserException.class})
    public RedirectView duplicatedUserExceptionHandler(UserLoginException e, RedirectAttributes redirectAttributes) {
        log.debug(e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return new RedirectView("/signup");
    }
}
