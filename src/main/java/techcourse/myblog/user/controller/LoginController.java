package techcourse.myblog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.user.dto.UserLoginDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.exception.InvalidLoginFormException;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String renderLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@Valid UserLoginDto userLoginDto, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            throw new InvalidLoginFormException(result.getFieldError().getDefaultMessage());
        }
        UserResponseDto user = userService.login(userLoginDto);
        session.setAttribute("user", user);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.removeAttribute("user");
        return new RedirectView("/");
    }
}