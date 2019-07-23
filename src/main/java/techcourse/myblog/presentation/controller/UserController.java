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
import techcourse.myblog.presentation.controller.exception.InvalidUpdateException;

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
        RedirectView redirectView = new RedirectView();
        String email = (String) httpSession.getAttribute("email");

        if (user.compareEmail(email)) {
            userService.modify(user);

            redirectView.setUrl("/mypage");
            return redirectView;
        }
        throw new InvalidUpdateException("잘못된 이메일 입력입니다.");
    }

    @DeleteMapping("/users")
    public RedirectView deleteUser(HttpSession httpSession, @Valid UserDto user) {
        RedirectView redirectView = new RedirectView("/");
        String email = (String) httpSession.getAttribute("email");

        if (user.compareEmail(email)) {
            userService.removeById(email);
            httpSession.invalidate();
        }

        return redirectView;
    }

    @ExceptionHandler(DuplicatedIdException.class)
    public RedirectView handleDuplicatedIdError(RedirectAttributes redirectAttributes, DuplicatedIdException e) {
        RedirectView redirectView = new RedirectView("/signup");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotExistUserIdException.class)
    public RedirectView handleNotExistIdError(RedirectAttributes redirectAttributes, NotExistUserIdException e) {
        RedirectView redirectView = new RedirectView(e.getNextView());
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(InvalidUpdateException.class)
    public RedirectView handleInvalidUpdateError(RedirectAttributes redirectAttributes, InvalidUpdateException e) {
        RedirectView redirectView = new RedirectView("/mypage/edit");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(NotMatchPasswordException.class)
    public RedirectView handleNotMatchPasswordError(RedirectAttributes redirectAttributes, NotMatchPasswordException e) {
        RedirectView redirectView = new RedirectView("/login");
        redirectAttributes.addFlashAttribute("errormessage", e.getMessage());
        return redirectView;
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(RedirectAttributes redirectAttributes, BindException e) {
        RedirectView redirectView = new RedirectView("signup");

        String errorMessages = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));

        redirectAttributes.addFlashAttribute("errormessage", errorMessages);
        return redirectView;
    }
}
