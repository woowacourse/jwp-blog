package techcourse.myblog.common;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.NoArticleException;
import techcourse.myblog.exception.NoUserException;
import techcourse.myblog.exception.SignUpFailException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoArticleException.class)
    public String notFindArticle(NoArticleException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(LoginFailException.class)
    public String failLogin(LoginFailException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(NoUserException.class)
    public String notFindUser(NoUserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(SignUpFailException.class)
    public String failSignUp(SignUpFailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(BindException.class)
    public String handle(BindException e, Model model) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        model.addAttribute("error", fieldError.getDefaultMessage());

        String invalidClassName = bindingResult.getTarget().getClass().getName();
        if (invalidClassName.equals(UserUpdateRequestDto.class.getName())) {
            return "mypage-edit";
        }
        if (invalidClassName.equals(UserSignUpRequestDto.class.getName())) {
            return "signup";
        }
        return "/";
    }
}
