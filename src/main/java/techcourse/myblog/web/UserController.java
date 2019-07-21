package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserSaveDto;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }

    @PostMapping("/users")
    public String saveUser(UserSaveDto userSaveDto, Model model) {
        try {
            userService.save(userSaveDto.toEntity());
        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "login";
    }
}
