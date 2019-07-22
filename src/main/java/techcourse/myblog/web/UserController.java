package techcourse.myblog.web;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger LOG = getLogger(UserController.class);
    private static final String DUPLICATED_EMAIL = "이미 가입되어 있는 이메일 주소입니다. 다른 이메일 주소를 입력해 주세요.";

    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean hasDuplicatedEmail(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @GetMapping("/signup")
    public String singupPage() {
        return "signup";
    }

    @GetMapping("/users")
    public String userList(final Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @Transactional
    @PostMapping("/users")
    public String signup(final Model model, @Valid UserDto userDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final FieldError error = bindingResult.getFieldError();
            LOG.info("클라이언트에서 전송된 필드에 오류 있음: {}", error.getField());
            model.addAttribute("error", error.getField());
            return "/signup";
        }
        if (hasDuplicatedEmail(userDto.getEmail())) {
            LOG.info("중복 이메일 계정: {}", userDto.getEmail());
            model.addAttribute("error", DUPLICATED_EMAIL);
            return "/signup";
        }
        final User user = new User(userDto);
        userRepository.save(user);
        LOG.info("회원 가입 성공");
        LOG.info("username: {}", userDto.getUsername());
        LOG.info("password: {}", userDto.getPassword());
        LOG.info("email: {}", userDto.getEmail());
//        return "redirect:/login";
        return "login";
    }
}
