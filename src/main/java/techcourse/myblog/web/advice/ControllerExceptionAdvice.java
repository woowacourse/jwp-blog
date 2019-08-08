package techcourse.myblog.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.service.exception.*;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class ControllerExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionAdvice.class);

    @ExceptionHandler({NotFoundUserException.class, UserDeleteException.class,
            NotFoundArticleException.class, NotFoundUserException.class})
    public String handleNotFoundUserException(Exception e) {
        log.error(e.getMessage());

        return "redirect:/";
    }

    @ExceptionHandler(SignUpException.class)
    public String handleSignUpException(Model model, Exception e) {
        log.error(e.getMessage());

        model.addAttribute("errorMessage", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserUpdateException.class)
    public String handleUpdateUserException(Exception e,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {
        log.error(e.getMessage());

        UserSessionDto user = (UserSessionDto) session.getAttribute("loggedInUser");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/mypage/" + user.getId() + "/edit";
    }

    @ExceptionHandler(LogInException.class)
    public String handleLogInException(Model model, Exception e) {
        log.error(e.getMessage());

        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(NotLoggedInException.class)
    public String handleNotLoggedInException(Exception e) {
        log.error(e.getMessage());

        return "redirect:/login";
    }
}
