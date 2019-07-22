package techcourse.myblog.web.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.web.controller.dto.LoginDto;

@ControllerAdvice
public class ExceptionController {
    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(NotFoundUserException.class)
    public String abc(NotFoundUserException e, Model model) {
        model.addAttribute("loginDto", new LoginDto());
        model.addAttribute("errorMessage", "이메일과 비밀번호를 다시 확인해주세요.");
        log.error(e.getMessage());
        return "login";
    }
}
