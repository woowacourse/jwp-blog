package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.web.support.LoginSessionManager;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final LoginSessionManager loginSessionManager;

    public LoginController(LoginService loginService, LoginSessionManager loginSessionManager) {
        this.loginService = loginService;
        this.loginSessionManager = loginSessionManager;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        loginSessionManager.clearSession();
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserDto userDto) {
        UserDto resultDto = loginService.login(userDto);
        loginSessionManager.setLoginSession(resultDto.getName(), resultDto.getEmail());
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }
}
