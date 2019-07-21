package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.LogInInfoDto;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.service.LogInService;
import techcourse.myblog.service.exception.LogInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogInController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private LogInService logInService;

    public LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(LogInInfoDto logInInfoDto, HttpServletRequest httpServletRequest) {
        UserPublicInfoDto userPublicInfoDto = logInService.logIn(logInInfoDto);
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(LOGGED_IN_USER, userPublicInfoDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }

    @ExceptionHandler(LogInException.class)
    public String handleLogInException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }
}
