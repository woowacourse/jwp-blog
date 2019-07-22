package techcourse.myblog.web;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotValidException;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class UserController {
    private static final Logger LOG = getLogger(UserController.class);
    private static final String DUPLICATED_EMAIL = "이미 가입되어 있는 이메일 주소입니다.";

    private final UserRepository userRepository;

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void verifyDuplicatedEmail(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            LOG.info("duplicated email: {}", email);
            throw new NotValidException(DUPLICATED_EMAIL, "email");
        }
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
    public String signup(final Model model, @Valid UserDto userDto) {
        LOG.info("HTTP POST: /signup");
        LOG.info("username: {}", userDto.getUsername());
        LOG.info("password: {}", userDto.getPassword());
        LOG.info("email: {}", userDto.getEmail());
        verifyDuplicatedEmail(userDto.getEmail());
        final User user = new User(userDto);
        userRepository.save(user);
        LOG.info("회원 가입 성공");
        return "login";
    }
}
