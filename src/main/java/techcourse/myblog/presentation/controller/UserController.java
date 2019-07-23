package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.UserService;
import techcourse.myblog.application.service.exception.DuplicatedIdException;
import techcourse.myblog.application.service.exception.NotExistUserIdException;
import techcourse.myblog.application.service.exception.NotMatchPasswordException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public RedirectView createUser(@Valid UserDto user) {
        RedirectView redirectView = new RedirectView("/login");
        userService.save(user);
        return redirectView;
    }

    @GetMapping("/users")
    public ModelAndView readUsers() {
        ModelAndView modelAndView = new ModelAndView("user-list");
        modelAndView.addObject("users", userService.findAll());
        return modelAndView;
    }

    @PostMapping("/login")
    public RedirectView login(HttpSession httpSession, LoginDto loginDto) {
        userService.login(loginDto);

        RedirectView redirectView = new RedirectView("/");
        httpSession.setAttribute("email", loginDto.getEmail());
        httpSession.setMaxInactiveInterval(600);
        return redirectView;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession httpSession) {
        httpSession.invalidate();

        return new RedirectView("/");
    }

    @GetMapping("/mypage")
    public ModelAndView readMyPage(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = (String) httpSession.getAttribute("email");
        modelAndView.setViewName("mypage");
        modelAndView.addObject("user", userService.findById(email));

        return modelAndView;
    }

    @GetMapping("/mypage/edit")
    public ModelAndView readMyPageEdit(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String email = (String) httpSession.getAttribute("email");
        modelAndView.setViewName("mypage-edit");
        modelAndView.addObject("user", userService.findById(email));
        return modelAndView;
    }

    @PutMapping("/mypage/edit")
    public RedirectView updateUser(HttpSession httpSession, @Valid UserDto user) {
        RedirectView redirectView = new RedirectView("/mypage");
        String email = (String) httpSession.getAttribute("email");
        userService.modify(user, email);

        return redirectView;
    }

    @DeleteMapping("/users")
    public RedirectView deleteUser(HttpSession httpSession, @Valid UserDto user) {
        RedirectView redirectView = new RedirectView("/");
        String email = (String) httpSession.getAttribute("email");

        userService.removeById(user, email);
        httpSession.invalidate();

        return redirectView;
    }
}
