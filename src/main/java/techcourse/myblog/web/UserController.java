package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.InvalidEditFormException;
import techcourse.myblog.exception.InvalidSignUpFormException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String renderSignUpPage(HttpSession httpSession) {
        return "signup";
    }

    @PostMapping("/users")
    public RedirectView createUser(HttpSession httpSession, @Valid UserDto.Create userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidSignUpFormException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.save(userDto);
        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String readUsers(Model model, HttpSession httpSession) {
        List<UserDto.Response> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @PutMapping("/users/{userId}")
    public RedirectView updateUser(HttpSession httpSession, @Valid UserDto.Update userDto,
                                   BindingResult bindingResult, @PathVariable Long userId) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = userSession.get();
        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return new RedirectView("/");
        }

        if (bindingResult.hasErrors()) {
            throw new InvalidEditFormException(bindingResult.getFieldError().getDefaultMessage());
        }

        UserDto.Response updatedUser = userService.update(userId, userDto);
        httpSession.setAttribute("user", updatedUser);
        return new RedirectView("/mypage/" + userId);
    }

    @DeleteMapping("/users/{userId}")
    public RedirectView deleteUser(@PathVariable Long userId, HttpSession httpSession) {

        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = userSession.get();

        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return new RedirectView("/");
        }

        userService.deleteById(userId);
        httpSession.removeAttribute("user");
        return new RedirectView("/");
    }

    @GetMapping("/mypage/{userId}")
    public String renderMypage(@PathVariable Long userId, Model model) {
        UserDto.Response user = userService.findById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/mypage/{userId}/edit")
    public String renderEditMypage(@PathVariable Long userId, HttpSession httpSession, Model model) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        UserDto.Response user = userService.findById(userId);
        UserDto.Response sessionUser = userSession.get();
        if (!sessionUser.getEmail().equals(user.getEmail())) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "mypage-edit";
    }

}
