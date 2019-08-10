package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.UserService;
import techcourse.myblog.application.dto.LoginRequest;
import techcourse.myblog.application.dto.UserEditRequest;
import techcourse.myblog.application.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private static final String USER_INFO = "user";
    private static final String USERS_INFO = "users";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm(LoginRequest loginRequest) {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(UserRequest userRequest) {
        return "signup";
    }

    @PostMapping("/users")
    public String save(@Valid UserRequest userRequest, BindingResult bindingResult) {
        // 회원 가입 입력 형식 유효성 검사
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.save(userRequest);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute(USERS_INFO, userService.findAll());

        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPageForm(Model model, HttpServletRequest request) {
        model.addAttribute(USER_INFO, request.getSession().getAttribute(USER_INFO));

        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm(UserEditRequest userEditRequest) {
        return "mypage-edit";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        httpSession.setAttribute(USER_INFO, userService.login(loginRequest));

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_INFO);

        return "redirect:/";
    }

    @PutMapping("/users/{userId}")
    public String modify(@PathVariable("userId") Long userId, @Valid UserEditRequest userEditRequest,
                           BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "mypage-edit";
        }
        request.getSession().setAttribute(USER_INFO, userService.modify(userId, userEditRequest.getName()));

        return "redirect:/";
    }

    @DeleteMapping("/users/{userId}")
    public String remove(@PathVariable("userId") Long userId, HttpServletRequest request) {
        userService.remove(userId);
        request.getSession().invalidate();

        return "redirect:/";
    }
}
