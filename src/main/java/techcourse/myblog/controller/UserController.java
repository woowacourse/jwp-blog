package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.support.validation.UserGroups;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.DuplicateEmailException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public String createUser(@Validated({Default.class, UserGroups.Edit.class}) UserDto userDto, BindingResult bindingResult) {
        try {
            userService.save(userDto);
        } catch (DuplicateEmailException e) {
            bindingResult.addError(new FieldError("userDto", "email", e.getMessage()));
            return "signup";
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }
        return "redirect:/login";
    }

    @GetMapping("")
    public String findAllUsersForm(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest httpServletRequest, UserDto userDto, BindingResult bindingResult) {
        Optional<User> user = userService.findByEmailAndPassword(userDto);

        if (user.isPresent()) {
            httpServletRequest.getSession().setAttribute("user", user.get());
            return "redirect:/";
        }
        bindingResult.addError(new FieldError("userDto", "email", "이메일이나 비밀번호가 일치하지 않습니다."));

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return "redirect:/";
    }
}
