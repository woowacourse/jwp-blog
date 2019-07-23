package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.user.UserResponseDto;
import techcourse.myblog.exception.EmailNotFoundException;
import techcourse.myblog.exception.InvalidPasswordException;
import techcourse.myblog.service.login.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class LoginController {
    final private LoginService loginservice;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginservice= loginService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin(final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (!Objects.isNull(session.getAttribute("user"))) {
            modelAndView.setView(new RedirectView("/"));
            return modelAndView;
        }
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/users/login")
    public ModelAndView processLogin(final HttpServletRequest request, final String email, final String password) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto userResponseDto;
        try {
            userResponseDto = loginservice.findByEmailAndPassword(email, password);
        } catch (EmailNotFoundException | InvalidPasswordException e) {
            // TODO: 2019-07-19 에러메시지 띄우기
            System.err.println(e.getMessage());
            modelAndView.setView(new RedirectView("/login"));
            return modelAndView;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", userResponseDto);
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }
}
