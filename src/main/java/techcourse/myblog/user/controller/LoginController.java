package techcourse.myblog.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.user.dto.UserDto;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/login")
    public String renderLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(UserDto.Login userDto, HttpServletRequest request) {
        UserDto.Response user = userService.login(userDto);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return new RedirectView("/");
    }
}
