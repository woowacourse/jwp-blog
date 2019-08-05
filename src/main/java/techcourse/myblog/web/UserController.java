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
import techcourse.myblog.resolver.Session;
import techcourse.myblog.resolver.UserSession;
import techcourse.myblog.service.UserService;

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

    @PutMapping("/users/{userId}")
    public RedirectView updateUser(HttpSession httpSession, @Valid UserDto.Update userDto,
                                   BindingResult bindingResult, @PathVariable Long userId) {
        if (bindingResult.hasErrors()) {
            throw new InvalidEditFormException(bindingResult.getFieldError().getDefaultMessage());
        }
        UserDto.Response userSession = (UserDto.Response) httpSession.getAttribute("user");

        UserDto.Response updatedUser = userService.update(userSession, userId, userDto);
        httpSession.setAttribute("user", updatedUser);

        return new RedirectView("/mypage/" + userId);
    }

    @DeleteMapping("/users/{userId}")
    public RedirectView deleteUser(@PathVariable Long userId, HttpSession httpSession) {
        UserDto.Response userSession = (UserDto.Response) httpSession.getAttribute("user");
        userService.deleteById(userSession, userId);
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
    public String renderEditMypage(@PathVariable Long userId, @Session UserSession userSession, Model model) {
        UserDto.Response userDto = userService.findById(userSession.getUserDto(), userId);
        model.addAttribute("user", userDto);
        return "mypage-edit";
    }

}
