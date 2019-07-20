package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.ControllerUtil.isLoggedIn;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String postLogin(LoginDto loginDto, Model model, HttpSession session) {
        try {
            User loginUser = loginService.findUser(loginDto);
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/login")
    public String loginView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "login";
    }
}
