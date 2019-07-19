package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.exception.SignUpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) != null) {
            return "redirect:/";
        }
        return "sign-up";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public String createUser(UserDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }

    @ExceptionHandler(SignUpException.class)
    public String handleSignUpException(Model model, Exception e) {
        model.addAttribute("errorMessage", e.getMessage());
        return "sign-up";
    }
}
