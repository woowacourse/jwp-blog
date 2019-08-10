package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.service.LogInService;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.LoginUserDto;

import javax.servlet.http.HttpSession;

@Controller
public class LogInController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private LogInService logInService;

    public LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession httpSession) {
        if (httpSession.getAttribute(LOGGED_IN_USER) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(LogInInfoDto logInInfoDto, HttpSession httpSession) {
        LoginUserDto loginUserDto = logInService.logIn(logInInfoDto);
        httpSession.setAttribute(LOGGED_IN_USER, loginUserDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }
}
