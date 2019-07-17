package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

@Controller
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage() {
        return "sign-up";
    }

    @PostMapping("/users")
    public String createUser(UserDto userDto) {
        userRepository.save(userDto.toEntity());
        return "redirect:/users/login";
    }
}
