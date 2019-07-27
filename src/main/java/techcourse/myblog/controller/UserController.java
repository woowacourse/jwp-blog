package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.exception.UserArgumentException;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

import javax.validation.Valid;

@Controller
@SessionAttributes("user")
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUpForm() {
        return "sign-up";
    }

    @PostMapping
    public String saveUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new UserArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userDto);
        return "redirect:/login";
    }

    @DeleteMapping
    public String deleteUser(@ModelAttribute User user, SessionStatus sessionStatus) {
        userService.delete(user.getId());
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
