package techcourse.myblog.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_MESSAGE = "message";
    private static final String REDIRECT_URL = "redirect:/err";

    @ExceptionHandler(value = UserException.class)
    public String UserExceptionHandler(UserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
        return REDIRECT_URL;
    }
}
