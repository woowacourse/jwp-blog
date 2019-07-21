package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.dto.UserSaveDto;
import techcourse.myblog.exception.UserDuplicateException;
import techcourse.myblog.exception.UserMismatchException;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final String SUCCESS_SIGN_UP_MESSAGE = "회원 가입이 완료되었습니다!";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession httpSession) {
        if (isUserInSession(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    private boolean isUserInSession(HttpSession httpSession) {
        return httpSession.getAttribute("user") != null;
    }

    @GetMapping("/signup")
    public String showSignUpPage(HttpSession httpSession) {
        if (isUserInSession(httpSession)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/login")
    public String login(UserLoginDto userLoginDto, Model model, HttpSession httpSession) {
        try {
            userService.checkLogin(userLoginDto.getEmail(), userLoginDto.getPassword());
        } catch (UserNotFoundException | UserMismatchException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }

        User user = userService.findByEmail(userLoginDto.getEmail());
        httpSession.setAttribute("user", user);
        return "index";
    }

    @PostMapping("/users")
    public String signUp(UserSaveDto userSaveDto, Model model) {
        try {
            userService.save(userSaveDto.toEntity());
            model.addAttribute("successMessage", SUCCESS_SIGN_UP_MESSAGE);
        } catch (UserDuplicateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "login";
    }

    @GetMapping("/users")
    public String fetchUsers(Model model) {
        Iterable<User> users = userService.findAll();

        model.addAttribute("users", users);
        return "user-list";
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
}
