package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSaveDto;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.UserService;

@Controller
public class UserController {
    private static final String SUCCESS_SIGN_UP_MESSAGE = "회원 가입이 완료되었습니다!";
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
            model.addAttribute("successMessage", SUCCESS_SIGN_UP_MESSAGE);
        } catch (UserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "login";
    }

    @GetMapping("/users")
    public String fetchUsers(Model model) {
        Iterable<User> users = userService.findAll();

        model.addAttribute("users", users);
        return "user-list";
    }
}
