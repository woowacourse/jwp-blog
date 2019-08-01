package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.LogInService;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpSession;

@Controller
public class LogInController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private LogInService logInService;

    public LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute(LOGGED_IN_USER) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(LogInInfoDto logInInfoDto, HttpSession session) {
        UserPublicInfoDto userPublicInfoDto = logInService.logIn(logInInfoDto);
        session.setAttribute(LOGGED_IN_USER, userPublicInfoDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }
}
