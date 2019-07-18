package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public String login(final UserDto.LoginInfo loginInfo, final HttpSession session, final Model model) {
        //TODO 메시지 보내는 방식으로 고치기
        if (canLogin(loginInfo.getEmail(), loginInfo.getPassword())) {
            // TODO 중복 get 제거
            User user = userRepository.findByEmail(loginInfo.getEmail()).orElseThrow(IllegalArgumentException::new);
            UserDto.SessionUserInfo sessionUserInfo = UserDto.SessionUserInfo.toDto(user);
            session.setAttribute("userInfo", sessionUserInfo);
            return "/index";
        }
        model.addAttribute("errorMessage", "비밀번호를 올바르게 입력하시거나 회원가입을 해주세요");

        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.removeAttribute("userInfo");
        return "/index";
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

    @PostMapping("/users")
    public String saveUser(final UserDto.SignUpUserInfo signUpUserInfo, final Model model) {
        User user = signUpUserInfo.toUser();
        if (containsUser(user.getEmail())) {
            model.addAttribute("errorMessage", "이메일이 중복됩니다");
            return "/user/signup";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    //TODO IllegalArgumentException -> CustomException으로 변화
    @GetMapping("/users/{id}")
    public String userPage(@PathVariable Long id, final Model model) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/users/edit/{id}")
    public String userEditPage(@PathVariable Long id, final Model model) {
        User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/users/edit")
    public String update(final User user) {
        User userParam = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        userParam.setName(user.getName());
        userRepository.save(userParam);
        System.out.println(userParam);
        return "redirect:/users/edit/" + userParam.getId() ;
    }

    private boolean canLogin(final String email, final String password) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            return user.matchPassword(password);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean containsUser(final String email) {
        return userRepository.existsByEmail(email);
    }
}
