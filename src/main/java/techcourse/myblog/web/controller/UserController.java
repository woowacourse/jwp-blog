package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(HttpSession httpSession, LoginDto loginDto) {
        if (httpSession.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginDto loginDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            User user = userService.login(loginDto);
            httpSession.setAttribute("user", user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "이메일과 비밀번호를 다시 확인해주세요.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        // 잘 되는지 확인
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showMyPageEdit() {
        // 잘 되는지 확인
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String updateUser(UserDto userDto, HttpSession httpSession) {
        // 잘 되는지 확인
        User user = (User) httpSession.getAttribute("user");
        userDto.setEmail(user.getEmail());
        httpSession.setAttribute("user", userService.update(userDto));

        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage/edit")
    public String deleteUser(HttpSession httpSession) {
        // 잘 되는지 확인
        User user = (User) httpSession.getAttribute("user");
        userService.remove(user.getEmail());
        return "redirect:/logout";
    }

    @GetMapping("/users")
    public String users(Model model) {
        // 잘 되는지 확인
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        if (userService.exists(userDto.getEmail())) {
            model.addAttribute("errorMessage", "중복된 이메일입니다.");
            return "signup";
        }
        userService.save(userDto);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String singUp(UserDto userDto) {
        // 잘 되는지 확인
        return "signup";
    }
}
