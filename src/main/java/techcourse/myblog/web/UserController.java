package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserLoginRequest;
import techcourse.myblog.service.dto.UserRequest;
import techcourse.myblog.service.dto.UserResponse;
import techcourse.myblog.service.exception.LoginException;
import techcourse.myblog.service.exception.SignUpException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private static final String USER_INFO = "user";
    private static final String USERS_INFO = "users";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String createLoginForm(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_INFO) == null) {
            return "login";
        }
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String createSignForm() {
        return "signup";
    }

    @PostMapping("/users")
    public String saveUser(@Valid UserRequest userRequest, BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            throw new SignUpException("email 중복");
        }
        User user = userService.saveUser(userRequest);
        httpSession.setAttribute(USER_INFO, user);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserResponse> users = new ArrayList<>();
        for (User user : userService.findAll()) {
            users.add(new UserResponse(user.getName(), user.getEmail()));
        }
        model.addAttribute(USERS_INFO, users);

        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPageForm(Model model, HttpServletRequest request) {
        model.addAttribute(USER_INFO, request.getSession().getAttribute(USER_INFO));

        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditForm() {
        return "mypage-edit";
    }

    @PostMapping("/login")
    public String login(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        User user = userService.findUserByEmail(userLoginRequest);

        if (!user.matchPassword(userLoginRequest.getPassword())) {
            throw new LoginException("비밀번호 틀림");
        }

        request.getSession().setAttribute(USER_INFO, user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_INFO);

        return "redirect:/";
    }

    @PutMapping("/users/{userId}")
    public String editUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        String name = request.getParameter("name");
        User user = userService.editUserName(userId, name);
        request.getSession().setAttribute(USER_INFO, user);

        return "redirect:/";
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
        userService.deleteById(userId);
        request.getSession().removeAttribute(USER_INFO);

        return "redirect:/";
    }
}
