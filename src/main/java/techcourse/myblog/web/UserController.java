package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession httpSession) {
        User user = userService.getUser(userDto);
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("email", user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/users")
    public String enrollUser(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model) {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(Model model) {
        return "mypage-edit";
    }
}
