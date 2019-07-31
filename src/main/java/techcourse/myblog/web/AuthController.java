package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotExistUserException;
import techcourse.myblog.exception.NotMatchAuthenticationException;
import techcourse.myblog.exception.UserLoginInputException;
import techcourse.myblog.service.AuthService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(@ModelAttribute String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UserLoginInputException("로그인 값이 잘못됐습니다.");
        }

        User user = authService.login(userDto);
        session.setAttribute("user", user);

        return new RedirectView("/");
    }

    @ExceptionHandler({UserLoginInputException.class, NotMatchAuthenticationException.class, NotExistUserException.class})
    public RedirectView loginException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/auth/login");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }
}
