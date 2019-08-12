package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.LogInService;
import techcourse.myblog.service.dto.LogInInfoDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.UserController.LOGGED_IN_USER;

@Controller
public class LogInController {
    private LogInService logInService;

    public LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String logIn(LogInInfoDto logInInfoDto, HttpServletRequest httpServletRequest) {
        UserPublicInfoDto userPublicInfoDto = logInService.logIn(logInInfoDto);
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute(LOGGED_IN_USER, userPublicInfoDto);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }
}
