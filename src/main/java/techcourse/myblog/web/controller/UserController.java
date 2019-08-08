package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.dto.MyPageDto;
import techcourse.myblog.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.web.SessionManager.USER;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String moveSignUpPage(UserDto userDto) {
        return "signup";
    }

    @PostMapping("/users")
    public String createUser(@Valid UserDto userRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.save(userRequestDto.toEntity());
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String selectAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/mypages/{id}")
    public String moveMyPage(@PathVariable long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "mypage";
    }

    @GetMapping("/users/updates/{id}")
    public String moveMyPageEdit(@PathVariable long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/users/updates/{id}")
    public String updateMyPage(@PathVariable long id, MyPageDto myPageDto, HttpSession httpSession) {
        User user = userService.updateUserInfo(id, myPageDto);
        httpSession.setAttribute(USER, user);
        return "redirect:/mypages/" + user.getId();
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable long id, HttpSession httpSession) {
        httpSession.removeAttribute(USER);
        userService.deleteById(id);
        return "redirect:/";
    }
}
