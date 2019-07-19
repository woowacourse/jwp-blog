package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
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

    @PostMapping("/login")
    public RedirectView login(
            String email,
            String password,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            HttpServletResponse res
    ) {
        return userRepository.findByEmail(email)
                            .filter(u -> u.authenticate(password))
                            .map(user -> {
                                    session.setAttribute("key", Objects.hash(email));
                                    session.setAttribute("username", user.getName());
                                    session.setAttribute("email", user.getEmail());
                                    Cookie loginCookie = new Cookie("credential", Objects.hash(email) + "");
                                    res.addCookie(loginCookie);
                                    return new RedirectView("/");
                            }).orElseGet(() -> {
                                    redirectAttributes.addAttribute(
                                            "errorMessage",
                                            "존재하지 않는 사용자이거나 잘못된 비밀번호입니다."
                                    );
                                    return new RedirectView("/login");
                            });
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView registerUser(User user, RedirectAttributes redirectAttributes) {
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
    public RedirectView myPageEditConfirm(HttpSession session, String email, String name) {
        return userRepository.findByEmail((String) session.getAttribute("email")).map(user -> {
                    if (email.equals(user.getEmail())) {
                        user.setName(name);
                        user.setEmail(email);
                        userRepository.save(user);
                        session.setAttribute("username", name);
                        session.setAttribute("email", email);
                        return new RedirectView("/mypage");
                    }
                    return userRepository.findByEmail(email).map(sameEmail -> new RedirectView("."))
                                                            .orElseGet(() -> {
                                                                    user.setName(name);
                                                                    user.setEmail(email);
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