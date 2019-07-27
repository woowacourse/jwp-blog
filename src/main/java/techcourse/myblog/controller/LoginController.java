package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import techcourse.myblog.controller.dto.LoginDto;
import techcourse.myblog.model.User;
import techcourse.myblog.service.LoginService;

@Controller
@SessionAttributes("user")
@RequestMapping("/login")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @PostMapping
    public String login(LoginDto loginDto, Model model) {
        User user = loginService.getLoginUser(loginDto);
        model.addAttribute("user", user);
        log.info("userName : {}", user.getUserName());
        return "redirect:/";
    }

}
