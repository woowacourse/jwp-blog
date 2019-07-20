package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.controller.dto.LoginDto;
import techcourse.myblog.web.controller.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp(UserDto userDto) {
        return "signup";
    }

    @GetMapping("/login")
    public String loginForm(LoginDto loginDto) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginDto loginDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        Optional<User> user = userService.checkUser(loginDto);
        if (user.isPresent()) {
            httpSession.setAttribute("user", user.get());
            return "redirect:/";
        }

        model.addAttribute("errorMessage", "이메일과 비밀번호를 다시 확인해주세요.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showMyPageEdit() {
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String updateUser(UserDto userDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        userDto.setEmail(user.getEmail());
        httpSession.setAttribute("user", userService.update(userDto));
        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage/edit")
    public String deleteUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        userService.remove(user.getEmail());
        return "redirect:/logout";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }


    @PostMapping("/user")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "중복된 이메일입니다.");
            return "signup";
        }
        userService.save(userDto);
        return "redirect:/login";
    }
}
