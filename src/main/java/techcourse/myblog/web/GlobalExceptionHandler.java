package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.exception.*;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({NotMatchAuthenticationException.class, UserNotFoundException.class, UserForbiddenException.class})
    public String authException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return "redirect:/";
    }

    @ExceptionHandler({ArticleNotFoundException.class, ArticleInputException.class})
    public String articleException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "redirect:/";
    }

    @ExceptionHandler({UserLoginInputException.class, NotExistUserException.class})
    public String loginException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "redirect:/auth/login";
    }


    @ExceptionHandler({SignUpInputException.class, AlreadyExistUserException.class})
    public RedirectView registerException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new RedirectView("/users/signup");
    }
}
