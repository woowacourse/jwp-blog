package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.web.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String sighupView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/users")
    public String registerUserData(@Valid UserDto userDto, BindingResult bindingResult, Model model, HttpSession session) {
        try {
            if (isLoggedIn(session)) {
                return "redirect:/";
            }
            userService.save(userDto, bindingResult);
            return "redirect:/login";
        }catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
}
