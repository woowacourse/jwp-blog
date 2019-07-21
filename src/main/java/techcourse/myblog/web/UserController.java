package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserEditDto;
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
    public static final String ERROR_MISMATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다!";
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

    @GetMapping("/mypage/edit")
    public String showMyPageConfirm() {
        return "mypage-confirm";
    }

    @PostMapping("/mypage/edit")
    public String showMyPageEdit(String password, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "index";
        }

        if (!user.matchPassword(password)) {
            model.addAttribute("errorMessage", ERROR_MISMATCH_PASSWORD_MESSAGE);
            return "mypage-confirm";
        }

        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editMyPage(UserEditDto userEditDto, HttpSession httpSession) {
        User lastUser = (User) httpSession.getAttribute("user");
        User user = User.builder()
                .name(userEditDto.getName())
                .email(lastUser.getEmail())
                .password(lastUser.getPassword())
                .build();

        httpSession.setAttribute("user", user);
        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(String email, HttpSession httpSession) {
        userService.deleteUser(email);
        httpSession.removeAttribute("user");

        return "redirect:/";
    }
}
