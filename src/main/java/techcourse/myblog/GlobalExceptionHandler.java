package techcourse.myblog;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.web.UserException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UserException.class)
    public String handleUserException(UserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/err";
    }
}
