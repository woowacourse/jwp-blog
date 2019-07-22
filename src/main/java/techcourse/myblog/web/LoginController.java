package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.AuthenticationDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login/check")
    public String login(AuthenticationDto authenticationDto, HttpSession httpSession) {
        User user = loginService.login(authenticationDto);
        httpSession.setAttribute(LoginUtil.USER_SESSION_KEY, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(LoginUtil.USER_SESSION_KEY);
        return "redirect:/";
    }
}
