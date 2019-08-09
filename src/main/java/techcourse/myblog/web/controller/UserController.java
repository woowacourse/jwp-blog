package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.support.LoginSessionManager;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final LoginSessionManager loginSessionManager;

    @Autowired
    public UserController(UserService userService, LoginSessionManager loginSessionManager) {
        this.userService = userService;
        this.loginSessionManager = loginSessionManager;
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }

    @GetMapping("/mypage-edit")
    public String editUser() {
        return "mypage-edit";
    }

    @PostMapping("/users")
    public String addUser(UserDto userDto) {
        userService.addUser(userDto);
        return "redirect:/login";
    }

    @PutMapping("/users")
    public String updateUser(UserDto userDto,
                             @SessionInfo UserSessionInfo userSessionInfo) {
        User user = userService.updateUser(userSessionInfo.toUser(), userDto);
        loginSessionManager.setLoginSession(user);
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser(@SessionInfo UserSessionInfo userSessionInfo) {
        userService.deleteUser(userSessionInfo.getEmail());
        loginSessionManager.clearSession();
        return "redirect:/";
    }
}
