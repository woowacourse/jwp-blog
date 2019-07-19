package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "signup";
        }

        if (userService.create(userDto)) {
            model.addAttribute("error", "중복된 이메일 입니다.");
            return "signup";
        }

        return "redirect:/login";
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "mypage-edit";
        }

        return userService.update(userUpdateRequestDto, httpSession);
    }

    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        return userService.delete(httpSession);
    }
}
