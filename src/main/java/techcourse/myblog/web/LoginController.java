package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.exception.UserMismatchException;
import techcourse.myblog.exception.UserNotFoundException;
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
    public String login(UserLoginDto userLoginDto, Model model, HttpSession httpSession) {
        try {
            loginService.checkLogin(userLoginDto.getEmail(), userLoginDto.getPassword());
        } catch (UserNotFoundException | UserMismatchException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }

        User user = loginService.findByEmail(userLoginDto.getEmail());
        httpSession.setAttribute("user", user);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
