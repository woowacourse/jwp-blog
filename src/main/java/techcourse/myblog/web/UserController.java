package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.web.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public String sighUpUser(@Valid UserDto userDto, BindingResult bindingResult, HttpSession session) {
        try {
            if (session.getAttribute("user") != null) {
                return "redirect:/";
            }
            if (bindingResult.hasErrors()) {
                throw new InvalidSighUpException("올바르지 않은 입력 값 입니다.");
            }
            User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
            userRepository.save(user);
            return "redirect:/login";
        }catch (InvalidSighUpException e) {
            return "sighup";
        }
    }
}
