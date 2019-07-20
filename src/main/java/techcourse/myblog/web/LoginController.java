package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.FailedLoginException;
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
            return "redirect:/";
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
        }
        return "redirect:/";
    }

    @ExceptionHandler(FailedLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedLoginException(FailedLoginException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }
}
