package techcourse.myblog.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.service.exception.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.UserController.LOGGED_IN_USER;

@ControllerAdvice
public class BlogControllerAdvice {
    @ExceptionHandler({NotFoundUserException.class,
            NotFoundArticleException.class, AccessNotPermittedException.class})
    public String handleNotFoundException() {
        return "error";
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
        UserProfileDto user = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/mypage/" + user.getId() + "/edit";
    }

    @ExceptionHandler(LogInException.class)
    public String handleLogInException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }
}
