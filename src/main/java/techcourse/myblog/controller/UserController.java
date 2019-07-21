package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.DuplicateEmailException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.RedirectAttributeSupport;
import techcourse.myblog.support.validation.UserGroups.All;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findAllUsersForm(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return new RedirectView("/");
    }

    @PostMapping
    public RedirectView createUser(@Validated({All.class}) UserDto userDto, BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            RedirectAttributeSupport.addBindingResult(redirectAttributes, bindingResult, "userDto", userDto);
            return new RedirectView("/signup");
        }

        try {
            userService.save(userDto);
        } catch (DuplicateEmailException e) {
            bindingResult.addError(new FieldError("userDto", "email", e.getMessage()));
            RedirectAttributeSupport.addBindingResult(redirectAttributes, bindingResult, "userDto", userDto);
            return new RedirectView("/signup");
        }

        return new RedirectView("/login");
    }

    @PostMapping("/login")
    public RedirectView login(HttpServletRequest httpServletRequest, @ModelAttribute("userDto") UserDto userDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<User> user = userService.findByEmailAndPassword(userDto);
        if (user.isPresent()) {
            httpServletRequest.getSession().setAttribute("user", user.get());
            return new RedirectView("/");
        }
        bindingResult.addError(new FieldError("userDto", "email", "이메일이나 비밀번호가 일치하지 않습니다."));
        RedirectAttributeSupport.addBindingResult(redirectAttributes, bindingResult, "userDto", userDto);

        return new RedirectView("/login");
    }
}
