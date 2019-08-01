package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.user.LoginDto;
import techcourse.myblog.domain.user.SignUpDto;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.domain.user.UserInfoDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    public static final String LOGIN_SESSION = "login-user";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String create(SignUpDto signUpDto) {
        userService.create(signUpDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginDto loginDto, HttpSession session) {
        UserDto findUserDto = userService.findByUserDto(loginDto);
        session.setAttribute(LOGIN_SESSION, findUserDto);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(LOGIN_SESSION);

        return "redirect:/";
    }

    @GetMapping("/users")
    public String showUserListPage(Model model) {
        model.addAttribute("users", userService.readAll());

        return "user-list";
    }

    @GetMapping("/users/mypage")
    public String showMypage(HttpSession session, Model model) {
        addUserInModel(session, model);

        return "mypage";
    }

    @GetMapping("/users/mypage-edit")
    public String showMypageEdit(HttpSession session, Model model) {
        addUserInModel(session, model);

        return "mypage-edit";
    }

    private void addUserInModel(HttpSession session, Model model) {
        UserDto userDto = userService.findByUserDto(getUserInfo(session));
        model.addAttribute("userData", userDto);
    }

    @PutMapping("/users/mypage-edit")
    public String update(HttpSession session, UserInfoDto userInfoDto) {
        userService.update(getUserInfo(session), userInfoDto);
        return "redirect:/users/mypage";
    }

    @DeleteMapping("/users/mypage-edit")
    public String delete(HttpSession session) {
        userService.deleteById(getUserInfo(session));
        return "redirect:/logout";
    }

    private UserDto getUserInfo(HttpSession session) {
        return (UserDto) session.getAttribute(UserController.LOGIN_SESSION);
    }
}