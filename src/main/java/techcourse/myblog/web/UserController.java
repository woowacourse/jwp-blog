package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;

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
    public String login(final UserDto.LoginInfo loginInfo, final HttpSession session) {
        if (containsUser(loginInfo.getEmail())) {
            User user = userRepository.findByEmail(loginInfo.getEmail());
            UserDto.SessionUserInfo sessionUserInfo = UserDto.SessionUserInfo.toDto(user);
            session.setAttribute("userInfo", sessionUserInfo);
            return "/index";
        }
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "/user/signup";
    }

    @GetMapping("/users")
    public String findUsers(final Model model) {
        final Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "/user/user-list";
    }


//    @PostMapping("/users")
//    public String saveUser(final User user, final Model model) {
////        User user = signUpUserInfo.toUser();
//        if (containsUser(user.getEmail())) {
//            model.addAttribute("errorMessage", "이메일이 중복됩니다");
//            return "redirect:/signup";
//        }
//        userRepository.save(user);
//        return "redirect:/login";
//    }

    @PostMapping("/users")
    public String saveUser1(final UserDto.SignUpUserInfo signUpUserInfo, final Model model) {
        User user = signUpUserInfo.toUser();
        if (containsUser(user.getEmail())) {
            model.addAttribute("errorMessage", "이메일이 중복됩니다");
            return "redirect:/signup";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    //TODO IllegalArgumentException -> CustomException으로 변화
    @GetMapping("/users/{id}")
    public String userPage(@PathVariable Long id, final Model model) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("user", user);
        return "/mypage";
    }

    private boolean containsUser(final String email) {
        return userRepository.existsByEmail(email);
    }
}
