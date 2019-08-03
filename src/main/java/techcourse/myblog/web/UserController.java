package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.LoginRequest;
import techcourse.myblog.dto.SignUpRequest;
import techcourse.myblog.dto.UpdateUserRequest;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.utils.model.ModelUtil;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final LoginService loginService;
    private final HttpSession session;

    public UserController(UserService userService, LoginService loginService, HttpSession session) {
        this.userService = userService;
        this.loginService = loginService;
        this.session = session;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/logout")
    public String logout() {
        SessionUtil.removeAttribute(session, USER);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        UserResponse userResponse = loginService.loginByEmailAndPwd(loginRequestDto);
        SessionUtil.setAttribute(session, USER, userResponse);

        return "redirect:/";
    }

    @GetMapping("/users")
    public String users(Model model) {
        ModelUtil.addAttribute(model, "users", userService.findAll());

        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
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
    public String addUser(@Valid SignUpRequest signUpRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        UserResponse userResponse = userService.addUser(signUpRequestDto);
        SessionUtil.setAttribute(session, USER, userResponse);

        return "redirect:/";
    }

    @PutMapping("/users")
    public String updateUser(@Valid UpdateUserRequest updateUserRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            log.error("updateUser : not logged in");
            return "mypage";
        }
        UserResponse origin = (UserResponse) SessionUtil.getAttribute(session, USER);
        UserResponse userResponse = userService.updateUser(updateUserRequestDto, origin);
        SessionUtil.setAttribute(session, USER, userResponse);

        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser() {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        userService.deleteUser(userResponse);
        SessionUtil.removeAttribute(session, USER);

        return "redirect:/";
    }
}
