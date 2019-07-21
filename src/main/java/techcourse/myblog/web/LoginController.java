package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession session) {
        setLoginSession(session, loginService.loginByEmailAndPwd(userDto));
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    private void setLoginSession(HttpSession session, UserDto userDto) {
        session.setAttribute("userName", userDto.getName());
        session.setAttribute("userEmail", userDto.getEmail());
    }
}
