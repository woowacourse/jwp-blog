package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
public class UserController {
    private static final String SESSION_USER_KEY = "user";

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String registerView() {
//        TODO: 로그인된 경우 처리 필요
        return "signup";
    }
    
    @PostMapping("/users")
    public String register(UserRequestDto userRequestDto, Model model, HttpSession session){
        try {
//             TODO: 로그인된 경우 처리 필요
            if (!userRequestDto.getPassword().equals(userRequestDto.getPasswordConfirm())) {
                throw new User.UserCreationConstraintException("비밀번호가 같지 않습니다.");
            }

            User user = User.of(userRequestDto.getName(), userRequestDto.getEmail(), userRequestDto.getPassword(),
                email -> userRepository.findByEmail(email).isPresent());
            userRepository.save(user);
            return "redirect:/login";
        } catch (User.UserCreationConstraintException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/login")
    public String loginView() {
//        TODO: 로그인된 경우 처리 필요
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDto requestDto, Model model, HttpSession session) {
        try {
//            TODO: 로그인된 경우 처리 필요
            User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다"));
            if (user.authentication(requestDto.getEmail(), requestDto.getPassword())) {
                session.setAttribute("user", user);
                return "redirect:/";
            }

            model.addAttribute("error", true);
            model.addAttribute("message", "비밀번호를 확인해주세요");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/users")
    public String userListView(Model model, HttpSession session) {
        checkAndPutUser(model, session);
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    private void checkAndPutUser(Model model, HttpSession session) {
        if (session.getAttribute(SESSION_USER_KEY) != null) {
            model.addAttribute(SESSION_USER_KEY, session.getAttribute(SESSION_USER_KEY));
        }
    }
}
