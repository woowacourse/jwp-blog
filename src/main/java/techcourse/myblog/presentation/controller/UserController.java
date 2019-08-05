package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.annotation.EmailAnnot;
import techcourse.myblog.application.dto.LoginDto;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.UserService;
import techcourse.myblog.domain.Email;
import techcourse.myblog.presentation.controller.exception.InvalidUpdateException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    public RedirectView login(HttpSession httpSession, @Valid LoginDto loginDto) {
        userService.login(loginDto);

        RedirectView redirectView = new RedirectView("/");
        httpSession.setAttribute("email", loginDto.getEmail());
        httpSession.setMaxInactiveInterval(600);
        return redirectView;
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession httpSession) {
        httpSession.removeAttribute("email");
        //httpSession.invalidate();

        return new RedirectView("/");
    }

    @GetMapping("/mypage")
    public ModelAndView readMyPage(@EmailAnnot Email email) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage");
        modelAndView.addObject("user", userService.findByEmail(email.getEmail()));

        return modelAndView;
    }

    @GetMapping("/mypage/edit")
    public ModelAndView readMyPageEdit(@EmailAnnot Email email) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mypage-edit");
        modelAndView.addObject("user", userService.findByEmail(email.getEmail()));
        return modelAndView;
    }

    @PutMapping("/mypage/edit")
    public RedirectView updateUser(@EmailAnnot Email email, @Valid UserDto user) {
        RedirectView redirectView = new RedirectView();

        if (user.compareEmail(email.getEmail())) {
            userService.modify(user);

            redirectView.setUrl("/mypage");
            return redirectView;
        }
        throw new InvalidUpdateException("잘못된 이메일 입력입니다.");
    }

    @DeleteMapping("/users")
    public RedirectView deleteUser(HttpSession httpSession, UserDto user) {
        RedirectView redirectView = new RedirectView("/");
        String email = (String) httpSession.getAttribute("email");

        if (user.compareEmail(email)) {
            userService.removeByEmail(email);
            httpSession.removeAttribute("email");
        }

        return redirectView;
    }
}
