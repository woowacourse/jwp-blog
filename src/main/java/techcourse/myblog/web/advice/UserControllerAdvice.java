package techcourse.myblog.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.exception.DuplicatedEmailException;
import techcourse.myblog.exception.NameToUpdateNotFoundException;

@ControllerAdvice
public class UserControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(DuplicatedEmailException.class)
    public String duplicatedEmail() {
        log.debug("이메일이 중복됩니다.");
        return "signup";
    }

    @ExceptionHandler(NameToUpdateNotFoundException.class)
    public String nameToUpdateNotFound() {
        log.debug("수정할 이름이 존재하지 않습니다.");
        return "mypage-edit";
    }
}
