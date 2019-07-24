package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginParams;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/login")
    public String login(UserLoginParams userLoginParams, HttpSession httpSession) {
        loginService.checkLogin(userLoginParams.getEmail(), userLoginParams.getPassword());

        User user = loginService.findByEmail(userLoginParams.getEmail());
        httpSession.setAttribute(USER, user);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(USER);
        return "redirect:/";
    }
}
