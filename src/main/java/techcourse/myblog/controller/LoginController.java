package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserDto userDto, HttpSession session, Model model) {
        String errorMessage = "아이디나 비밀번호가 잘못되었습니다.";
        Optional<User> user = loginService.findByEmail(userDto.getEmail());

        if (!user.isPresent() || !userDto.getPassword().equals(user.get().getPassword())) {
            model.addAttribute("error", errorMessage);
            return "login";
        }

        session.setAttribute("user", user.get());
        return "redirect:/";

    }

    @GetMapping("/logout")
    public String processLogout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
