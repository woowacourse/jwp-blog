package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
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

    @PostMapping("/login")
    public String processLogin(UserDto userDto, HttpSession session) {
        User user = loginService.login(userDto);

        session.setAttribute("user", user);
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
