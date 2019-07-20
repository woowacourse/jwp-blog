package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    // TODO: Dto를 Domain 객체로 만들어 주는 Translation Layer 구현하기

    @ExceptionHandler({ UserLoginInputException.class, NotMatchPasswordException.class, NotExistUserException.class })
    public RedirectView loginException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/auth/login");
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

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView registerUser(UserDto userDto, RedirectAttributes redirectAttributes) {
        User user = new User(userDto.getName(), userDto.getEmail(), userDto.getPassword());
        return userRepository.findByEmail(user.getEmail()).map(ifExists -> {
            redirectAttributes.addAttribute("errorMessage", "이미 존재하는 이메일입니다.");
            return new RedirectView("/signup");
        }).orElseGet(() -> {
            userRepository.save(user);
            return new RedirectView("/login");
        });
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        return userRepository.findByEmail((String) session.getAttribute("email"))
                .map(user -> {
                    model.addAttribute("user", user);
                    return "mypage";
                })
                .orElse("redirect:/");
    }

    @GetMapping("/mypageedit")
    public String myPageEdit(HttpSession session, Model model) {
        return userRepository.findByEmail((String) session.getAttribute("email"))
                .map(user -> {
                    model.addAttribute("user", user);
                    return "mypage-edit";
                })
                .orElse("redirect:/");
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
    public RedirectView deleteUser(HttpSession session) {
        userRepository.findByEmail((String) session.getAttribute("email")).ifPresent(user -> {
            userRepository.delete(user);
            session.invalidate();
        });
        return new RedirectView("/");
    }
}