package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {
    private static final String SESSION_NAME = "name";
    private static final String SESSION_EMAIL = "signedEmail";
    private static final String SESSION_AUTH = "isAuth";

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(HttpSession httpSession) {
        if (checkLoggedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(UserLoginDto loginDto, HttpSession httpSession) {
        User user = userService.login(loginDto);

        httpSession.setAttribute(SESSION_NAME, user.getName());
        httpSession.setAttribute(SESSION_EMAIL, user.getEmail());
        httpSession.setAttribute(SESSION_AUTH, true);

        return "redirect:/";
    }

    @GetMapping("/join")
    public String showSignUpPage(HttpSession httpSession) {
        if (checkLoggedIn(httpSession)) {
            return "redirect:/";
        }

        return "signup";
    }

    @PostMapping("/join")
    public String signUpUser(UserDto userDto) {
        userService.signUpUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession, Model model) {
        initMyPage(httpSession, model);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String editMyPage(HttpSession httpSession, Model model) {
        initMyPage(httpSession, model);
        return "mypage-edit";
    }

    @PutMapping("/mypage/save")
    public String completeEditMypage(HttpSession httpSession, UserDto userDto) {
        String sessionEmail = httpSession.getAttribute(SESSION_EMAIL).toString();

        String updatedUserName = userService.saveMyPage(sessionEmail, userDto);
        httpSession.setAttribute(SESSION_NAME, updatedUserName);

        return "redirect:/mypage";
    }

    @GetMapping("/users")
    public String showUsersListPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @DeleteMapping("/delete/user")
    public String deleteUser(HttpSession httpSession) {
        String email = httpSession.getAttribute(SESSION_EMAIL).toString();

        userService.delete(email);
        httpSession.invalidate();

        return "redirect:/";
    }

    private boolean checkLoggedIn(HttpSession httpSession) {
        return !Objects.isNull(httpSession.getAttribute(SESSION_EMAIL));
    }

    private void initMyPage(HttpSession httpSession, Model model) {
        String email = httpSession.getAttribute(SESSION_EMAIL).toString();
        User user = userService.findUser(email);
        model.addAttribute("user", user);
    }
}
