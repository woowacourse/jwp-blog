package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSignUpRequestDto;
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

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "signup";
        }

        userService.create(userSignUpRequestDto);
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage() {
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "mypage-edit";
        }

        Long userId = ((User) httpSession.getAttribute(LoginUtil.USER_SESSION_KEY)).getUserId();
        User user = userService.update(userUpdateRequestDto, userId);
        httpSession.setAttribute(LoginUtil.USER_SESSION_KEY, user);
        return "redirect:/users/mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        Long userId = ((User) httpSession.getAttribute(LoginUtil.USER_SESSION_KEY)).getUserId();
        userService.delete(userId);
        httpSession.removeAttribute(LoginUtil.USER_SESSION_KEY);
        return "redirect:/";
    }
}
