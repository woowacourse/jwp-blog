package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.repo.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(UserDto userDto, Model model) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            model.addAttribute("error", "중복된 이메일 입니다.");
            return "signup";
        }
        try {
            User user = new User(userDto);
            userRepository.save(user);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/edit")
    public String showEditPage() {
        return "mypage";
    }


}
