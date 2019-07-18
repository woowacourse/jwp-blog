package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.LoginDto;
import techcourse.myblog.dto.SignupDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userName");
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, Model model, HttpSession session) {
        LoginDto dto = userService.loginByEmailAndPwd(userDto);
        if (dto.isSuccess()) {
            session.setAttribute("userName", dto.getName());
            return "redirect:/";
        }
        model.addAttribute("error", dto.getMessage());
        return "login";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/users")
    public String addUser(UserDto userDto, Model model, HttpSession session) {
        SignupDto signupDto = userService.addUser(userDto);

        if (userService.addUser(userDto).isSuccess()) {
            session.setAttribute("userName", userDto.getName());
            return "redirect:/";
        }
        model.addAttribute("error", signupDto.getMessage());
        return "signup";
    }
}
