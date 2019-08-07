package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.userinfo.UserEmail;
import techcourse.myblog.domain.userinfo.UserPassword;
import techcourse.myblog.dto.UserLoginRequestDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
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
    public RedirectView login(UserLoginRequestDto userLoginRequestDto, HttpSession httpSession) {
        UserEmail userEmail = new UserEmail(userLoginRequestDto.getEmail());
        UserPassword userPassword = new UserPassword(userLoginRequestDto.getPassword());

        loginService.checkLogin(userEmail, userPassword);
        log.info("login post request params={}", userLoginRequestDto);

        User user = loginService.findByEmail(userLoginRequestDto.getEmail());
        httpSession.setAttribute(USER, user);

        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession httpSession) {
        log.info("logout get request params={}", httpSession.getAttribute(USER));

        httpSession.removeAttribute(USER);

        return new RedirectView("/");
    }
}
