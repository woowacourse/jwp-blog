package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(final User user, final HttpServletRequest request) {
        System.out.println(user);
        if(alreadyHasEmail(user)) {
            request.getSession().setAttribute("logInfo", user.getEmail());
            return "/index";
        }
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "user/signup";
    }

    @GetMapping("/users")
    public String findUsers(final Model model) {
        final Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user/user-list";
    }

    @PostMapping("/users")
    public String saveUser(final User user, final Model model) {
        if (alreadyHasEmail(user)) {
            model.addAttribute("errorMessage", "이메일이 중복됩니다");
            return "user/signup";
        }
        userRepository.save(user);
        return "user/login";
    }

    private boolean alreadyHasEmail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
}
