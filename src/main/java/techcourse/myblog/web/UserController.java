package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.utils.ModelUtil;
import techcourse.myblog.utils.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;
    private final LoginService loginService;
    @Autowired
    private HttpSession session;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        ;
        if (SessionUtil.isNull(session)) {
            return "login";
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        SessionUtil.removeAttribute(session, UserInfo.NAME);
        SessionUtil.removeAttribute(session, UserInfo.EMAIL);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(UserRequestDto userRequestDto) {
        User user = loginService.loginByEmailAndPwd(userRequestDto);
        SessionUtil.setAttribute(session, UserInfo.NAME, user.getName());
        SessionUtil.setAttribute(session, UserInfo.EMAIL, user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/users")
    public String users(Model model) {
        ModelUtil.addAttribute(model, "users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        if (SessionUtil.isNull(session)) {
            return "signup";
        }
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String editUser() {
        return "mypage-edit";
    }

    @PostMapping("/signup")
    public String addUser(UserRequestDto userRequestDto) {
        User user = userService.addUser(userRequestDto);
        SessionUtil.setAttribute(session, UserInfo.NAME, user.getName());
        SessionUtil.setAttribute(session, UserInfo.EMAIL, user.getEmail());
        return "redirect:/";
    }

    @PutMapping("/users")
    public String updateUser(UserRequestDto userRequestDto) {
        String email = String.valueOf(SessionUtil.getAttribute(session, UserInfo.EMAIL));
        userService.updateUser(userRequestDto, email);
        SessionUtil.setAttribute(session, UserInfo.NAME, userRequestDto.getName());
        SessionUtil.setAttribute(session, UserInfo.EMAIL, userRequestDto.getEmail());
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser() {
        String email = String.valueOf(SessionUtil.getAttribute(session, UserInfo.EMAIL));
        userService.deleteUser(email);
        SessionUtil.removeAttribute(session, UserInfo.NAME);
        SessionUtil.removeAttribute(session, UserInfo.EMAIL);
        return "redirect:/";
    }
}
