package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/new")
    public String signupForm() {
        return "signup";
    }

    @PostMapping
    @ResponseBody
    public Long signup(@Valid UserDto userDto) {
        return userService.save(userDto);
    }
}
