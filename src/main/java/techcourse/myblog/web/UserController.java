package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.model.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String createLoginForm(UserDto userDto) {
        return "login";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String createSignupForm(UserDto userDto) {
        return "signup";
    }

    @PostMapping("/users")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userRepository.save(userDto.toUser());
        } catch(DataIntegrityViolationException e) {
            bindingResult.addError(new FieldError("userDto", "email", "이미 존재하는 email입니다."));
            return "signup";
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession session, BindingResult bindingResult) {
        Optional<User> loginUser = userRepository.findByEmail(userDto.getEmail());

        if (!loginUser.isPresent()) {
            bindingResult.addError(new FieldError("userDto", "email", "이메일을 확인해주세요."));
            return "login";
        }

        if (!loginUser.get().matchPassword(userDto.toUser())) {
            bindingResult.addError(new FieldError("userDto", "password", "비밀번호를 확인해주세요."));
            return "login";
        }

        session.setAttribute("user", loginUser.get());
        return "redirect:/";
    }
}
