package techcourse.myblog.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.dto.LoginRequestDto;
import techcourse.myblog.service.LoginService;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private static final String NOT_EXIST_EMAIL_ERROR = "존재하지않는 email입니다.";
    private static final String NOT_EXIST_PASSWORD_ERROR = "비밀번호가 일치하지않습니다.";

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String moveLoginPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, Model model, HttpSession httpSession) {
        String requestEmail = loginRequestDto.getEmail();
        if (!loginService.existUserEmail(requestEmail)) {
            model.addAttribute("error", true);
            model.addAttribute("message", NOT_EXIST_EMAIL_ERROR);
            return "login";
        }
        if (!loginService.matchEmailAndPassword(requestEmail, loginRequestDto.getPassword())) {
            model.addAttribute("error", true);
            model.addAttribute("message", NOT_EXIST_PASSWORD_ERROR);
            return "login";
        }
        httpSession.setAttribute("user", loginService.findUserByEmail(requestEmail));
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
