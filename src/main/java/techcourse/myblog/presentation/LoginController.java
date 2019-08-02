package techcourse.myblog.presentation;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
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
    public RedirectView processLogin(UserRequestDto userRequestDto, HttpServletRequest request) {
        log.info("");

        loginService.login(request.getSession(), userRequestDto);

        return new RedirectView("/" + request.getHeader("Referrer"));
    }

    @GetMapping("/logout")
    public RedirectView processLogout(HttpServletRequest request) {
        log.info("");

        loginService.logout(request.getSession());

        return new RedirectView("/");
    }
}
