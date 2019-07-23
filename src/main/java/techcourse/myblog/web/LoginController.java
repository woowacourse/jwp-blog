package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@Controller
public class LoginController {
    UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserDto userDto, HttpServletRequest request, Model model) {
        String errorMessage = "아이디나 비밀번호가 잘못되었습니다.";
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (!user.isPresent() || !userDto.getPassword().equals(user.get().getPassword())) {
            model.addAttribute("error", errorMessage);
            return "login";
        }

        request.getSession().setAttribute("user", user.get());
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request) {
        log.debug(">>> session : {}", request.getSession().getAttribute("user"));

        request.getSession().removeAttribute("user");
        return "redirect:/";
    }
}
