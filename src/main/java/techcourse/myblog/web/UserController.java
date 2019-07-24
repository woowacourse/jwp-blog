package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String create(UserDto userDto) {
        userService.create(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession session) {
        UserDto findUserDto = userService.findUser(userDto);
        session.setAttribute("userId", findUserDto.getId());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            session.removeAttribute("userId");
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String showUserListPage(Model model) {
        model.addAttribute("users", userService.readAll());
        return "user-list";
    }

    @GetMapping("users/{id}/mypage")
    public String showMypage(@PathVariable final long id, Model model) {
        UserDto userDto = userService.readWithoutPasswordById(id);
        model.addAttribute("userData", userDto);
        return "mypage";
    }

    @GetMapping("/users/mypage-edit")
    public String showMypageEdit(HttpSession session, Model model) {
        Object userId = session.getAttribute("userId");
        UserDto userDto = userService.readWithoutPasswordById((long) userId);
        model.addAttribute("userData", userDto);
        return "mypage-edit";
    }

    @PutMapping("/users/mypage-edit")
    public String update(HttpSession session, UserDto userDto) {
        Object userId = session.getAttribute("userId");
        UserDto updateUserDto = userService.updateUser((long) userId, userDto);
        return "redirect:/users/" + updateUserDto.getId() + "/mypage";
    }

    @DeleteMapping("/users/mypage-edit")
    public String delete(HttpSession session) {
        Object userId = session.getAttribute("userId");
        userService.deleteById((long) userId);
        return "redirect:/logout";
    }
}