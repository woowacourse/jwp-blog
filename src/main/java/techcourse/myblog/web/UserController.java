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
import techcourse.myblog.dto.user.LoginRequest;
import techcourse.myblog.dto.user.SignUpRequest;
import techcourse.myblog.dto.user.UpdateUserRequest;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.utils.custum.LoginUser;
import techcourse.myblog.utils.model.ModelUtil;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;
import static techcourse.myblog.web.URL.*;

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

    @GetMapping(LOGIN)
    public String login() {
        return LOGIN;
    }

    @PostMapping(LOGOUT)
    public String logout() {
        SessionUtil.removeAttribute(session, USER);

        return REDIRECT + INDEX;
    }

    @PostMapping(LOGIN)
    public String login(@Valid LoginRequest loginRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return LOGIN;
        }
        UserResponse userResponse = loginService.loginByEmailAndPwd(loginRequestDto);
        SessionUtil.setAttribute(session, USER, userResponse);

        return REDIRECT + INDEX;
    }

    @GetMapping(USERS)
    public String users(Model model) {
        ModelUtil.addAttribute(model, "users", userService.findAll());

        return USER_LIST;
    }

    @GetMapping(SIGNUP)
    public String signupForm() {
        return SIGNUP;
    }

    @GetMapping(MYPAGE)
    public String mypage() {
        return MYPAGE;
    }

    @GetMapping(MYPAGE_EDIT)
    public String editUser() {
        return MYPAGE_EDIT;
    }

    @PostMapping(SIGNUP)
    public String addUser(@Valid SignUpRequest signUpRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return SIGNUP;
        }
        UserResponse userResponse = userService.addUser(signUpRequestDto);
        SessionUtil.setAttribute(session, USER, userResponse);

        return REDIRECT + INDEX;
    }

    @PutMapping(USERS)
    public String updateUser(@Valid UpdateUserRequest updateUserRequestDto, BindingResult result, @LoginUser UserResponse originUser) {
        if (result.hasErrors()) {
            log.error("updateUser : not logged in");
            return MYPAGE;
        }
        UserResponse updatedUser = userService.updateUser(updateUserRequestDto, originUser);
        SessionUtil.setAttribute(session, USER, updatedUser);

        return REDIRECT + MYPAGE;
    }

    @DeleteMapping("/users")
    public String deleteUser(@LoginUser UserResponse loginUser) {
        userService.deleteUser(loginUser);
        SessionUtil.removeAttribute(session, USER);

        return REDIRECT + INDEX;
    }
}
