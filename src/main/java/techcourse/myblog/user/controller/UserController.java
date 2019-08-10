package techcourse.myblog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.user.dto.UserCreateDto;
import techcourse.myblog.user.dto.UserResponseDto;
import techcourse.myblog.user.dto.UserUpdateDto;
import techcourse.myblog.user.exception.AuthenticationFailException;
import techcourse.myblog.user.exception.InvalidEditFormException;
import techcourse.myblog.user.exception.InvalidSignUpFormException;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/new")
    public String renderSignUpPage() {
        return "signup";
    }

    @PostMapping
    public RedirectView createUser(@Valid UserCreateDto userCreateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSignUpFormException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userCreateDto);
        return new RedirectView("/login");
    }

    @GetMapping
    public String readUsers(Model model) {
        List<UserResponseDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/{userId}")
    public String renderMyPage(@PathVariable long userId, Model model) {
        UserResponseDto user = userService.findById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/{userId}/edit")
    public String renderEditMyPage(@PathVariable long userId, UserSession session, Model model) {
        UserResponseDto user = userService.findById(userId);
        if (!session.getEmail().equals(user.getEmail())) {
            throw new AuthenticationFailException();
        }
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/{userId}")
    public RedirectView updateUser(@PathVariable long userId, HttpSession session,
                                   @Valid UserUpdateDto userUpdateDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidEditFormException(result.getFieldError().getDefaultMessage());
        }
        UserResponseDto updatedUser = userService.update(userId, userUpdateDto);
        session.setAttribute("user", updatedUser);
        return new RedirectView("/users/" + userId);
    }

    @DeleteMapping("/{userId}")
    public RedirectView deleteUser(@PathVariable long userId, UserSession session) {
        UserResponseDto user = userService.findById(userId);
        if (!session.getEmail().equals(user.getEmail())) {
            return new RedirectView("/");
        }
        if (userService.deleteById(userId)) {
            return new RedirectView("/logout");
        }
        return new RedirectView("/");
    }
}