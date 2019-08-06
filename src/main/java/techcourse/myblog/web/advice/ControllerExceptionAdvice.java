package techcourse.myblog.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.web.exception.*;

@ControllerAdvice
public class ControllerExceptionAdvice {
    @ExceptionHandler(NoMatchingCredentialLoginException.class)
    public RedirectView noMatchingCredentialException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "존재하지 않는 사용자이거나 잘못된 비밀번호입니다."
        );
        return new RedirectView("/login");
    }

    @ExceptionHandler(EmailAlreadyTakenSignupException.class)
    public RedirectView emailAlreadyTakenSignupException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "이미 등록된 이메일입니다."
        );
        return new RedirectView("/signup");
    }

    @ExceptionHandler(IllegalArgumentSignupException.class)
    public RedirectView illegalArgumentSignupException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "잘못된 입력입니다."
        );
        return new RedirectView("/signup");
    }

    @ExceptionHandler(EmailAlreadyTakenProfileEditException.class)
    public RedirectView emailAlreadyTakenProfileEditException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "이미 등록된 이메일입니다."
        );
        return new RedirectView("/profile/edit");
    }

    @ExceptionHandler(IllegalArgumentProfileEditException.class)
    public RedirectView illegalArgumentProfileEditException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "잘못된 입력입니다."
        );
        return new RedirectView("/profile/edit");
    }
}