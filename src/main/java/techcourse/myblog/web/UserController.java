package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(final User user, final HttpServletRequest request) {
        if(alreadyHasEmail(user)) {
            User userParam = userRepository.findByEmail(user.getEmail());
            userParam.setPassword(null);
            request.getSession().setAttribute("userInfo", userParam);
            return "/index";
        }
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "/user/signup";
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
            return "/user/signup";
        }
        userRepository.save(user);
        return "/user/login";
    }

    @GetMapping("/users/{id}")
    public String userPage(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return "/mypage/" + user.getId();
    }

    private boolean alreadyHasEmail(final User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
}
