package techcourse.myblog.users;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/new")
    public String signupForm() {
        return "signup";
    }

    @PostMapping
    @ResponseBody
    public Long signup(@Valid UserDto.Register userDto) {
        return userService.save(userDto);
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(UserDto.Register userDto, HttpSession session) {
        UserDto.Response userResponseDto = userService.login(userDto);
        session.setAttribute("user", userResponseDto);
        return userResponseDto.getName();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping
    public String userList(Model model) {
        List<UserDto.Response> users = userService.findAllExceptPassword();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        UserDto.Response userResponseDto = userService.findById(id);
        model.addAttribute("user", userResponseDto);
        return "mypage";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        UserDto.Response userResponseDto = userService.findById(id);
        model.addAttribute("user", userResponseDto);
        return "mypage-edit";
    }

    @PutMapping
    public String edit(@Valid UserDto.Update userDto, HttpSession session) {
        UserDto.Response responseDto = userService.update(((UserDto.Response)session.getAttribute("user")).getId(), userDto);
        return "redirect:/users/" + responseDto.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        userService.deleteById(id);
        session.removeAttribute("user");
        return "redirect:/";
    }
}
