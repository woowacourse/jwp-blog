package techcourse.myblog.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static org.slf4j.LoggerFactory.getLogger;

@ControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAdvice {
    private static final String ERROR = "error";
    private static final String ROUTE_LOGIN = "/login";
    private static final String ROUTE_SIGNUP = "/signup";
    private static final Logger LOG = getLogger(UserAdvice.class);

    @ExceptionHandler(NotValidLoginException.class)
    public String notValidUser(final NotValidLoginException e, final Model model) {
        LOG.debug("NotValidLoginException: {}", e.getMessage());
        model.addAttribute(ERROR, e.getMessage());
        return ROUTE_LOGIN;
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class, RollbackException.class})
    public String notValidSignup(final Exception e, final Model model) {
        LOG.debug("ValidationException: {}", e.getMessage());
        model.addAttribute(ERROR, e.getMessage());
        return ROUTE_SIGNUP;
    }
}
