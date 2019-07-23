package techcourse.myblog.web.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.*;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@org.springframework.web.bind.annotation.ControllerAdvice("techcourse.myblog.web")
public class ControllerAdvice {
    @ExceptionHandler({NotFoundUserException.class, UserDeleteException.class, NotFoundArticleException.class})
    public String handleNotFoundUserException() {
        return "redirect:/";
    }

    @ExceptionHandler(SignUpException.class)
    public String handleSignUpException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "sign-up";
    }

    @ExceptionHandler(UserUpdateException.class)
    public String handleUpdateUserException(Exception e,
                                            HttpServletRequest httpServletRequest,
                                            RedirectAttributes redirectAttributes) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserPublicInfoDto user = (UserPublicInfoDto) httpSession.getAttribute("loggedInUser");
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/mypage/" + user.getId() + "/edit";
    }

    @ExceptionHandler(LogInException.class)
    public String handleLogInException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(NotLoggedInException.class)
    public String handleNotLoggedInException() {
        return "redirect:/login";
    }
}
