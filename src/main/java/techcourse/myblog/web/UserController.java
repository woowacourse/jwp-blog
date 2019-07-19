package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String sighUpUser(@Valid UserDto userDto, BindingResult bindingResult, Model model, HttpSession session) {
        try {
            if (session.getAttribute("user") != null) {
                return "redirect:/";
            }
            checkUserDataError(bindingResult);
            checkSamePasswords(userDto);
            checkDuplicateEmail(userDto);
            User user = User.of(userDto.getName(), userDto.getEmail(), userDto.getPassword());
            userRepository.save(user);
            return "redirect:/login";
        }catch (InvalidSighUpException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "sighup";
        }
    }

    private void checkUserDataError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSighUpException("올바르지 않은 입력 값 입니다.");
        }
    }

    private void checkSamePasswords(UserDto userData) {
        if (!userData.getPassword().equals(userData.getPasswordConfirm())) {
            throw new InvalidSighUpException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkDuplicateEmail(UserDto userData) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new InvalidSighUpException("해당 이메일은 존재하는 이메일 입니다.");
        }
    }
}
