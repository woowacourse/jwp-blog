package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.NotFoundUserException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.controller.dto.LoginDto;
import techcourse.myblog.web.controller.dto.UserDto;
import techcourse.myblog.web.exception.CouldNotRegisterException;
import techcourse.myblog.web.exception.LoginFailException;

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
    public String signUp() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginDto loginDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            throw new LoginFailException(bindingResult.getFieldError().getDefaultMessage());
        }

        Optional<User> user = userService.login(loginDto);
        httpSession.setAttribute("user",
                user.orElseThrow(() -> new NotFoundUserException("이메일과 비밀번호를 다시 확인해주세요.")));
        return "redirect:/";
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
    public String updateUser(UserDto updatedUser, HttpSession httpSession) {
        // TODO: 2019-07-22 Controller Advice
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        httpSession.setAttribute("user", userService.update(email, updatedUser));
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
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CouldNotRegisterException(getErrorMessage(bindingResult));
        }
        userService.save(userDto);
        return "redirect:/login";
    }

    private String getErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldError().getField() + "이(가) " + bindingResult.getFieldError().getDefaultMessage();
    }
}
