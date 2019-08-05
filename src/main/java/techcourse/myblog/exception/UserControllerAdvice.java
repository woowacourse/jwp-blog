package techcourse.myblog.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UserControllerAdvice {

    @ResponseBody
    @ExceptionHandler({UserDuplicateException.class, UserMismatchException.class, UserNotFoundException.class})
    public String handleUserException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(IllegalUserParamsException.class)
    public String handleIllegalUserParamsException(IllegalUserParamsException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
