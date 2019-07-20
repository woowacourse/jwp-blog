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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class UserController {

    private UserRepository userRepository;
    private UserTranslator userTranslator;

    public UserController(final UserRepository userRepository, final UserTranslator userTranslator) {
        this.userRepository = userRepository;
        this.userTranslator = userTranslator;
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

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String showSignupForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @PostMapping("/users")
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

    @ExceptionHandler({ SignUpInputException.class, AlreadyExistUserException.class })
    public RedirectView registerException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/auth/signup");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping(path = { "/mypage", "/mypageedit" })
    public RedirectView showMyPageEdit(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        String loginEmail = (String) session.getAttribute("email");
        Optional<User> maybeUser = userRepository.findByEmail(loginEmail);

        model.addAttribute("user", maybeUser.orElseThrow(() -> new UserNotFoundException("해당 이메일로 가입된 유저가 없습니다.")));

        if (req.getRequestURI().contains("mypageedit")){
            return new RedirectView("/mypageedit");
        }
        return new RedirectView("/mypage");
    }

    @PutMapping("/mypageedit")
    public RedirectView myPageEditConfirm(UserDto userDto, HttpSession session) {
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

    @ExceptionHandler({ UserNotFoundException.class })
    public RedirectView userNotFoundException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/");
    }
}