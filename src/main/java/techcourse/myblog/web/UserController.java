package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;
import techcourse.myblog.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage() {
        return "signup";
    }

    @PostMapping("/users")
    public String createUser(UserDto.Create userDto, Model model) {
        User newUser = userDto.toUser();
        try {
            userService.save(newUser);
        } catch (DuplicatedUserException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
        return "login";
    }
}
