package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.UserLoginInputException;
import techcourse.myblog.service.AuthService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.web.AuthController.AUTH_DEFAULT_URL;

@Controller
@RequestMapping(AUTH_DEFAULT_URL)
public class AuthController {
    public static final String AUTH_DEFAULT_URL = "/auth";

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UserLoginInputException("로그인 값이 잘못됐습니다.");
        }

        UserDto user = authService.login(userDto);
        session.setAttribute("user", user);

        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }
}
