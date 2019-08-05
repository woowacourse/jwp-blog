package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {
    private static final String ERROR_MESSAGE = "아이디나 비밀번호가 잘못되었습니다.";
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserDto userDto, HttpSession httpSession, Model model) {
        User user;

        try {
            loginService.checkValidUser(userDto);
            user = loginService.findUserIdByUserEmail(userDto.getEmail());
        } catch (Exception e) {
            model.addAttribute("error", ERROR_MESSAGE);
            return "login";
        }

        httpSession.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String processLogout(HttpSession httpSession) {
        try {
            httpSession.removeAttribute("user");
        } catch (IllegalStateException e) {
            return "redirect:/login";
        }

        return "redirect:/";
    }
}
