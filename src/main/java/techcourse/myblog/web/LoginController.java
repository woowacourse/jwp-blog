package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.UnknownHostAccessException;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLogin(HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            throw new FailedLoginException();
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession httpSession) {
        User user = loginService.findUserByEmailAndPassword(userDto);

        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("email", user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            httpSession.removeAttribute("name");
            httpSession.removeAttribute("email");

            return "redirect:/";
        }

        throw new UnknownHostAccessException();
    }

    @ExceptionHandler(FailedLoginException.class)
    public String handleFailedLoginException() {
        return "redirect:/";
    }

    @ExceptionHandler(UnknownHostAccessException.class)
    public String handleUnknownHostAccessException() {
        return "redirect:/";
    }
}
