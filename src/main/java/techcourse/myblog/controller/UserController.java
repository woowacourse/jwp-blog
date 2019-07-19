package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) {
        log.debug("Sign Up -> userDto : {}", userDto);
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.save(userDto);
        return "login";
    }

    @GetMapping("")
    public String findAllUsersForm(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/login")
    public RedirectView login(HttpServletRequest httpServletRequest, UserDto userDto) {
        log.debug("Login -> userDto : {}", userDto);
        Optional<User> user = userService.findByEmailAndPassword(userDto);
        if (user.isPresent()) {
            httpServletRequest.getSession().setAttribute("user", user.get());
            return new RedirectView("/");
        }
        return new RedirectView("/login");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return "redirect:/";
    }
}
