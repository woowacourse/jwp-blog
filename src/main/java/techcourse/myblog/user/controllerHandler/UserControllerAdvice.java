package techcourse.myblog.user.controllerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.user.exception.DuplicatedEmailException;

@ControllerAdvice
public class UserControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(DuplicatedEmailException.class)
    public String duplicatedEmail() {
        log.debug("이메일이 중복됩니다.");
        return "signup";
    }

    @ExceptionHandler(UserHasNotAuthorityException.class)
    public String UserHasNotAuthority() {
        log.debug("접근 권한이 없습니다.");
        return "redirect:/";
    }
}
