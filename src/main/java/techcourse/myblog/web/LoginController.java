package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.login.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class LoginController {
    final private LoginService loginservice;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginservice = loginService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/users/login")
    public String processLogin(final HttpSession session, final String email, final String password) {
        UserResponse userResponse = loginservice.findByEmailAndPassword(email, password);
        UserResponse retrieveUser = loginservice.findByEmail(userResponse.getEmail());

        session.setAttribute(USER_SESSION_KEY, retrieveUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logOut(final HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_KEY);
        return "redirect:/";
    }
}
