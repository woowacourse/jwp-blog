package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.user.UserResponseDto;
import techcourse.myblog.service.login.LoginService;

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
        User userResponseDto = loginservice.findByEmailAndPassword(email, password);
        User retrieveUser = loginservice.findByEmail(userResponseDto.getEmail());

        session.setAttribute(USER_SESSION_KEY, retrieveUser);
        return "redirect:/";
    }
}
