package techcourse.myblog.exception;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateDto;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class BlogExceptionHandler {

    @ExceptionHandler(CouldNotFindArticleIdException.class)
    public String coultNotFIndArticleId() {
        return "redirect:/";
    }

    @ExceptionHandler(BindException.class)
    public String methodArgumentNotValid(BindException e, Model model, HttpSession session) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        model.addAttribute("errorMessage", fieldError.getDefaultMessage());

        String invalidClassName = bindingResult.getTarget().getClass().getName();
        if (invalidClassName.equals(UserDto.class.getName())) {
            return "signup";
        }
        if (invalidClassName.equals(UserUpdateDto.class.getName())) {
            model.addAttribute("loginUser", session.getAttribute("loginUser"));
            return "mypage-edit";
        }

        return "/";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public String duplicateEmail(DuplicateEmailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(UnequalPasswordException.class)
    public String unequalPassword(UnequalPasswordException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
