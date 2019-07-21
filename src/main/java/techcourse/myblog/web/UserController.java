package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.*;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.translator.UserTranslator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserTranslator userTranslator;

    public UserController(final UserRepository userRepository, final UserTranslator userTranslator) {
        this.userRepository = userRepository;
        this.userTranslator = userTranslator;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @PostMapping
    public RedirectView registerUser(@Valid UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new SignUpInputException("회원 가입에 필요한 값이 잘못됐습니다. 확인해주세요");
        }

        Optional<User> maybeUser = userRepository.findByEmail(userDto.getEmail());

        if (maybeUser.isPresent()) {
            throw new AlreadyExistUserException("이미 존재하는 이메일입니다.");
        }

        User user = userTranslator.toEntity(new User(), userDto);
        userRepository.save(user);
        return new RedirectView("/auth/login");
    }

    @ExceptionHandler({SignUpInputException.class, AlreadyExistUserException.class})
    public RedirectView registerException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return new RedirectView("/users/signup");
    }

    @GetMapping(path = {"/{email}", "/{email}/edit"})
    public String showMyPageEdit(@PathVariable String email, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        String loginEmail = (String) session.getAttribute("email");
        userAuthenticated(email, loginEmail);

        Optional<User> maybeUser = userRepository.findByEmail(loginEmail);
        model.addAttribute("user", maybeUser.orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다.")));

        if (req.getRequestURI().contains("edit")) {
            return "mypage-edit";
        }
        return "mypage";
    }

    @PutMapping("/{email}")
    public RedirectView myPageEditConfirm(@PathVariable String email, @Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UpdateUserInputException("잘못된 입력값입니다.");
        }

        System.out.println(email);
        System.out.println(userDto.getName());

        String loginEmail = (String) session.getAttribute("email");
        userAuthenticated(email, loginEmail);

        Optional<User> maybeUser = userRepository.findByEmail(email);
        User updateUser = userTranslator.toEntity(maybeUser.orElseThrow(() ->
                new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다.")), userDto);
        User updatedUser = userRepository.save(updateUser);
        session.setAttribute("username", updatedUser.getName());

        return new RedirectView("/users/" + email);
    }

    @DeleteMapping("/{email}")
    public RedirectView exitUser(@PathVariable String email, HttpSession session) {
        String loginEmail = (String) session.getAttribute("email");
        userAuthenticated(email, loginEmail);

        userRepository.findByEmail(loginEmail).ifPresent(user -> {
            userRepository.delete(user);
            session.invalidate();
        });

        return new RedirectView("/");
    }

    private void userAuthenticated(final String email, final String loginEmail) {
        if (!loginEmail.equals(email)) {
            throw new UserForbiddenException("인증 되지 않은 사용자입니다.");
        }
    }

    @ExceptionHandler({UserNotFoundException.class, UserForbiddenException.class})
    public RedirectView userAuthException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/");
    }
}