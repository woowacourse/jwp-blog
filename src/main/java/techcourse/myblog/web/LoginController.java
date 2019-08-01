package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginRequestDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/login")
    public String login(UserLoginRequestDto userLoginRequestDto, HttpSession httpSession) {
        log.info("login post request params={}", userLoginRequestDto);
        loginService.checkLogin(userLoginRequestDto.getEmail(), userLoginRequestDto.getPassword());

        User user = loginService.findByEmail(userLoginRequestDto.getEmail());
        httpSession.setAttribute(USER, user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        log.info("logout get request params={}", httpSession.getAttribute(USER));
        httpSession.removeAttribute(USER);
        return "redirect:/";
    }
}
