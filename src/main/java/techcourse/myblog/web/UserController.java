package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String registerView() {
        return "signup";
    }
    
    @PostMapping("/users")
    public String register(UserRequestDto userRequestDto, Model model){
        try {
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
        return "login";
    }

    @GetMapping("/users")
    public String userListView(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }
}
