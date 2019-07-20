package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.AlreadyExistUserException;
import techcourse.myblog.exception.SignUpInputException;
import techcourse.myblog.exception.UserForbiddenException;
import techcourse.myblog.exception.UserNotFoundException;
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
    public String showSignupForm(@RequestParam(required = false) String errorMessage, Model model) {
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
        redirectAttributes.addAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/auth/signup");
    }

    @GetMapping(path = {"/{email}", "/{email}/edit"})
    public String showMyPageEdit(HttpServletRequest req, String email, Model model) {
        HttpSession session = req.getSession();
        String loginEmail = (String) session.getAttribute("email");
        if (!loginEmail.equals(email)) {
            throw new UserForbiddenException("인증 되지 않은 사용자입니다.");
        }

        Optional<User> maybeUser = userRepository.findByEmail(loginEmail);
        model.addAttribute("user", maybeUser.orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다.")));

        if (req.getRequestURI().contains("edit")) {
            return "mypage-edit";
        }
        return "mypage";
    }

    @PutMapping("/mypageedit")
    public RedirectView myPageEditConfirm(@Valid UserDto userDto, Errors errors, HttpSession session) {
        return userRepository.findByEmail((String) session.getAttribute("email")).map(user -> {
            String name = userDto.getName();
            String email = userDto.getEmail();
            if (email.equals(user.getEmail())) {
                userRepository.save(user);
                session.setAttribute("username", name);
                session.setAttribute("email", email);
                return new RedirectView("/mypage");
            }
            return userRepository.findByEmail(email).map(sameEmail -> new RedirectView("."))
                    .orElseGet(() -> {
                        userRepository.save(user);
                        session.setAttribute("username", name);
                        session.setAttribute("email", email);
                        return new RedirectView("/mypage");
                    });
        }).orElse(new RedirectView("/"));
    }

    @DeleteMapping("/users")
    public RedirectView exitUser(HttpSession session) {
        userRepository.findByEmail((String) session.getAttribute("email")).ifPresent(user -> {
            userRepository.delete(user);
            session.invalidate();
        });
        return new RedirectView("/");
    }

    @ExceptionHandler({UserNotFoundException.class, UserForbiddenException.class})
    public RedirectView userAuthException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/");
    }
}