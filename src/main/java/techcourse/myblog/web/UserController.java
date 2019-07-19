package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(
            String email,
            String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        return userRepository.findByEmail(email)
                            .filter(user -> user.authenticate(password))
                            .map(user -> {
                                session.setAttribute("name", user.getName());
                                session.setAttribute("email", user.getEmail());
                                return new RedirectView("/");
                            }).orElseGet(() -> {
                                redirectAttributes.addAttribute(
                                        "errorMessage",
                                        "존재하지 않는 사용자이거나 잘못된 비밀번호입니다."
                                );
                                return new RedirectView("/login");
                            });
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @PostMapping("/users")
    public RedirectView registerUser(User user, RedirectAttributes redirectAttributes) {
        return userRepository.findByEmail(user.getEmail()).map(ifSameEmailExists -> {
            redirectAttributes.addAttribute("errorMessage", "이미 등록된 이메일입니다.");
            return new RedirectView("/signup");
        }).orElseGet(() -> {
            userRepository.save(user);
            return new RedirectView("/login");
        });
    }

    private Optional<User> ifLoggedIn(HttpSession session) {
        return userRepository.findByEmail((String) session.getAttribute("email"));
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        return ifLoggedIn(session).map(user -> {
            model.addAttribute("user", user);
            return "mypage";
        }).orElse("redirect:/");
    }

    @GetMapping("/profile/edit")
    public String profileEditForm(Model model, HttpSession session) {
        return ifLoggedIn(session).map(user -> {
            model.addAttribute("user", user);
            return "mypage-edit";
        }).orElse("redirect:/");
    }

    @PutMapping("/profile/edit")
    public RedirectView profileEditConfirm(String email, String name, HttpSession session) {
        return ifLoggedIn(session).map(user -> {
            if (!email.equals(user.getEmail()) && userRepository.findByEmail(email).isPresent()) {
                return new RedirectView("/profile/edit");
            }
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
            session.setAttribute("name", name);
            session.setAttribute("email", email);
            return new RedirectView("/profile");
        }).orElse(new RedirectView("/login"));
    }

    @DeleteMapping("/profile")
    public RedirectView cancelProfile(HttpSession session) {
        ifLoggedIn(session).ifPresent(user -> {
            userRepository.delete(user);
            session.invalidate();
        });
        return new RedirectView("/");
    }
}