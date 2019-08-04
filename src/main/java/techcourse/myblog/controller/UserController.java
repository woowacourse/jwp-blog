package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.exception.UserArgumentException;
import techcourse.myblog.exception.UserUpdateFailException;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

import javax.validation.Valid;

@Controller
@SessionAttributes("user")
@RequestMapping("/users")
public class UserController {
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

    @PutMapping
    public String updateUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new UserUpdateFailException(bindingResult.getFieldError().getDefaultMessage());
        }
        User user = userService.update(userDto);

        model.addAttribute("user", user);
        return "redirect:/users/mypage";
    }

    @DeleteMapping
    public String deleteUser(@ModelAttribute User user, SessionStatus sessionStatus) {
        userService.delete(user.getId());
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPageForm() {
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm() {
        return "mypage-edit";
    }

    @GetMapping
    public String userListForm(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }
}
