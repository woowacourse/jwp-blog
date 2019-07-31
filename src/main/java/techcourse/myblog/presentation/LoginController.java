package techcourse.myblog.presentation;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.dto.UserRequestDto;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage() {
        log.info("");

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserRequestDto userRequestDto, HttpServletRequest request) {
        log.info("");

        loginService.login(request.getSession(), userRequestDto);

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request) {
        log.info("");

        loginService.logout(request.getSession());

        return "redirect:/";
    }
}
