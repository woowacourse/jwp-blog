package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/err")
    public String err() {
        return "errorpage";
    }

    @PostMapping("/users")
    public String signUp(User user) {
        //TODO 여러개를 다 체크해야 되는 경우는 어떻게 해야 될까?
        //여러개의 조건을 전부다 쿼리를 날리는게 과연 모범 답안일까 ?
        //에러를 던지는건 어떨까? -> 공통적으로 묶어서..?
        if (!validateUser(user)) {
            return "redirect:/err";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    private boolean validateUser(User user) {
        return validateName(user.getName())
                && validatePassword(user.getPassword())
                && validateEmail(user.getEmail());
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]){2,10}$");
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean validateEmail(String email) {
        return userRepository.findUserByEmail(email) != null;
    }


    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("user", userRepository.findAll());
        return "user-list";
    }
}
