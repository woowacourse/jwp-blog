package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;

    @GetMapping("/login")
    public String renderLoginPage(HttpSession httpSession) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userSession.isPresent()) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(UserDto.Login userDto, HttpSession httpSession) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userSession.isPresent()) {
            return new RedirectView("/");
        }
        UserDto.Response user = userService.login(userDto);
        httpSession.setAttribute("user", user);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession httpSession) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (!userSession.isPresent()) {
            return new RedirectView("/");
        }
        httpSession.removeAttribute("user");
        return new RedirectView("/");
    }
}
