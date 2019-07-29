package techcourse.myblog.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AuthExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(AuthExceptionAdvice.class);

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthException.class)
    public String common(Exception e) {

        log.info(e.toString());

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);  //예외를 뷰에 던져서 주자.

        return "redirect:/";
    }
}
