package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.support.LoginSessionManager;
import techcourse.myblog.web.support.UserSessionInfo;

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
        model.addAttribute("users", userService.findAll());
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
        return "redirect:/login";
    }

    @PutMapping("/users")
    public String updateUser(UserDto userDto, UserSessionInfo userSessionInfo) {
        userService.updateUser(userSessionInfo.getEmail(), userDto);
        loginSessionManager.setLoginSession(userDto.getName(), userDto.getEmail());
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String deleteUser(UserSessionInfo userSessionInfo) {
        userService.deleteUser(userSessionInfo.getEmail());
        loginSessionManager.clearSession();
        return "redirect:/";
    }
}
