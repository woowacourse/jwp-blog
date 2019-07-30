package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.exception.LoginException;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.dto.AuthenticationDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(AuthenticationDto authenticationDto, HttpSession httpSession) {
        User user = loginService.login(authenticationDto);
        httpSession.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleLoginException(LoginException e, Model model) {
        model.addAttribute("error", e.getMessage());
        log.error(e.getMessage());
        return "login";
    }

}
