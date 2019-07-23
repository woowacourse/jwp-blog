package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final Logger log =
            LoggerFactory.getLogger(LoginController.class);

    private static final String SESSION_NAME = "userInfo";
    private static final String ERROR_MESSAGE_NAME = "errorMessage";

    private final LoginService loginService;

    @Autowired
    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(final UserRequestDto.LoginRequestDto loginRequestDto, final HttpSession session, final Model model) {
        log.debug("before login...");
        if (loginService.canLogin(loginRequestDto)) {
            log.debug("login...");
            User user = loginService.findByLoginRequestDto(loginRequestDto);
            UserRequestDto.SessionDto sessionDto = UserRequestDto.SessionDto.toDto(user);
            log.debug("user {} transform to {}", user, sessionDto);
            session.setAttribute(SESSION_NAME, sessionDto);
            return "/index";
        }
        model.addAttribute(ERROR_MESSAGE_NAME, "비밀번호를 올바르게 입력하시거나 회원가입을 해주세요");
        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.removeAttribute(SESSION_NAME);
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "/user/signup";
    }
}
