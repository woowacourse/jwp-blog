package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userName");
        return "index";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession session) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(UserException::new);
        if (!user.isEqualTo(userDto)) {
            return "/login";
        }
        session.setAttribute("userName", user.getName());
        return "redirect:/";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/users")
    public String addUser(UserDto dto) {
        userRepository.save(dto.toEntity());
        return "redirect:/users";
    }
}
