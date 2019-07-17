package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/signup")
    public String renderSignUpPage() {
        return "signup";
    }

    @PostMapping("/users")
    public String createUser(UserDto.Create userDto) {
        User newUser = userDto.toUser();
        userRepository.save(newUser);
        return "login";
    }
}
