package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String createLoginForm(LoginRequest loginRequest) {
        return "login";
    }

    @GetMapping("/signup")
    public String createSignForm(UserRequest userRequest) {
        return "signup";
    }

    @PostMapping("/users")
    public String saveUser(@Valid UserRequest userRequest, BindingResult bindingResult) {
        // 회원 가입 입력 형식 유효성 검사
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        userService.saveUser(userRequest);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/login";
        }

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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_INFO);

        return "redirect:/";
    }


    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        userService.deleteById(userId);
        request.getSession().removeAttribute(USER_INFO);

        return "redirect:/";
    }
}
