package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginParams;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession httpSession) {
        if (isUserInSession(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    private boolean isUserInSession(HttpSession httpSession) {
        return httpSession.getAttribute("user") != null;
    }

    @GetMapping("/signup")
    public String showSignUpPage(HttpSession httpSession) {
        if (isUserInSession(httpSession)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/login")
    public String login(UserLoginParams userLoginParams, Model model, HttpSession httpSession) {
        loginService.checkLogin(userLoginParams.getEmail(), userLoginParams.getPassword());

        User user = loginService.findByEmail(userLoginParams.getEmail());
        httpSession.setAttribute("user", user);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
