package techcourse.myblog.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class UserControllerAdvice {

    @ResponseBody
    @ExceptionHandler({UserDuplicateException.class, UserMismatchException.class, UserNotFoundException.class})
    public RedirectView handleUserException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        return new RedirectView("/login");
    }

    @ExceptionHandler(IllegalUserParamsException.class)
    public String handleIllegalUserParamsException(IllegalUserParamsException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
