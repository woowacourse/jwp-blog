package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String renderLoginPage() {
        return "login";
    }

    @PostMapping("/users")
    public RedirectView createUser(UserDto.Create userDto, Model model) {
        try {
            userService.save(userDto);
        } catch (DuplicatedUserException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return new RedirectView("/signup");
        }
        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String readUsers(Model model) {
        List<UserDto.Response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }
}
