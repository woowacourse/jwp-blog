package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.web.ControllerUtil.isLoggedIn;
import static techcourse.myblog.web.ControllerUtil.putLoginUser;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, HttpSession session, Model model) {
        try {
            if (isLoggedIn(session)) {
                return "redirect:/";
            }
            userService.save(userDto, bindingResult);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/signup")
    public String signUpView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "signup";
    }

    @GetMapping("/users")
    public String userListView(HttpSession session, Model model) {
        putLoginUser(session, model);
        model.addAttribute("userList", userService.getUserList());
        return "user-list";
    }
}
