package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.dto.user.UserResponseDto;
import techcourse.myblog.service.login.LoginService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class LoginController {
    final private LoginService loginservice;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginservice = loginService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/users/login")
    public ModelAndView processLogin(final HttpSession session, final String email, final String password) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto userResponseDto = loginservice.findByEmailAndPassword(email, password);

        UserResponseDto retrieveUser = loginservice.findByEmail(userResponseDto.getEmail());
        session.setAttribute(USER_SESSION_KEY, retrieveUser);
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }
}
