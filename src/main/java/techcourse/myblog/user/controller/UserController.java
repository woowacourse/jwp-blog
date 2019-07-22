package techcourse.myblog.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.user.dto.UserDto;
import techcourse.myblog.user.exception.InvalidEditFormException;
import techcourse.myblog.user.exception.InvalidSignUpFormException;
import techcourse.myblog.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage() {
        return "signup";
    }

    @PostMapping("/users")
    public RedirectView createUser(@Valid UserDto.Create userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSignUpFormException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userDto);
        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String readUsers(Model model) {
        List<UserDto.Response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/mypage/{userId}")
    public String renderMypage(@PathVariable long userId, Model model) {
        UserDto.Response user = userService.findById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/mypage/{userId}/edit")
    public String renderEditMypage(@PathVariable long userId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = (UserDto.Response) session.getAttribute("user");
        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/users/{userId}")
    public RedirectView updateUser(@PathVariable long userId, HttpServletRequest request,
                                   @Valid UserDto.Update userDto, BindingResult result) {
        if (result.hasErrors()) {
            request.setAttribute("user", userService.findById(userId));
            throw new InvalidEditFormException(result.getFieldError().getDefaultMessage());
        }
        UserDto.Response updatedUser = userService.update(userId, userDto);
        HttpSession session = request.getSession(false);
        session.setAttribute("user", updatedUser);
        return new RedirectView("/mypage/" + userId);
    }

    @DeleteMapping("/users/{userId}")
    public RedirectView deleteUser(@PathVariable long userId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = (UserDto.Response) session.getAttribute("user");
        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return new RedirectView("/");
        }
        userService.deleteById(userId);
        return new RedirectView("/logout");
    }
}
