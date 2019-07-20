package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotExistUserException;
import techcourse.myblog.exception.NotMatchPasswordException;
import techcourse.myblog.exception.UserLoginInputException;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UserLoginInputException("로그인 값이 잘못됐습니다.");
        }

        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());
        User user = maybeUser.orElseThrow(() -> new NotExistUserException("해당 이메일로 가입한 유저가 없습니다."));

        if (user.authenticate(userDto.getPassword())) {
            session.setAttribute("username", user.getName());
            session.setAttribute("email", user.getEmail());
            return new RedirectView("/");
        }

        throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler({ UserLoginInputException.class, NotMatchPasswordException.class, NotExistUserException.class })
    public RedirectView loginException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/auth/login");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }
}
