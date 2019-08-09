package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.LogInService;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
public class LogInController {
    private LogInService logInService;
    private LoginChecker loginChecker;

    public LogInController(LogInService logInService, LoginChecker loginChecker) {
        this.logInService = logInService;
        this.loginChecker = loginChecker;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (loginChecker.isLoggedIn(session)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(LogInInfoDto logInInfoDto, HttpSession session) {
        UserSessionDto userSession = logInService.logIn(logInInfoDto);
        session.setAttribute(LoginChecker.LOGGED_IN_USER, userSession);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(LoginChecker.LOGGED_IN_USER);
        return "redirect:/";
    }
}
