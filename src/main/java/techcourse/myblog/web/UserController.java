package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.dto.user.UserRequest;
import techcourse.myblog.service.dto.user.UserResponse;
import techcourse.myblog.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserResponse> userResponses = userService.findAll();
        model.addAttribute("users", userResponses);
        return "user-list";
    }

    @PostMapping("/users")
    public String registerUsers(@Valid final UserRequest userRequest, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.save(userRequest);
        return "redirect:/login";
    }
}
