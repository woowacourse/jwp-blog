package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
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
    public String sighUpUser(@Valid UserDto userDto, HttpSession session) {
        try {
            if (session.getAttribute("user") != null) {
                return "redirect:/";
            }
            checkDuplicateEmail(userDto.getEmail());
            User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
            userRepository.save(user);
            return "redirect:/login";
        }catch (InvalidSighUpException e) {
            return "sighup";
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidSighUpException("해당 이메일은 존재하는 이메일 입니다.");
        }
    }
}
