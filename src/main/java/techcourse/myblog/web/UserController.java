package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/users")
    public String sighUpUser(@Valid UserDto userDto, BindingResult bindingResult, Model model, HttpSession session) {
        try {
            if (isLoggedIn(session)) {
                return "redirect:/";
            }
            userService.save(userDto, bindingResult);
            return "redirect:/login";
        }catch (InvalidSighUpException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "sighup";
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
}
