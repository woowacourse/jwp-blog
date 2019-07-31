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

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public String edit() {
        return "mypage-edit";
    }

    @PostMapping("/users")
    public String add(UserDto userDto) {
        userService.addUser(userDto);
        return "redirect:/login";
    }

    @PutMapping("/users")
    public String update(UserDto userDto, UserSessionInfo userSessionInfo, HttpServletRequest request) {
        userService.updateUser(userSessionInfo.getEmail(), userDto);
        LoginSessionManager loginSessionManager = new LoginSessionManager(request);
        loginSessionManager.setLoginSession(userDto.getName(), userDto.getEmail());
        return "redirect:/mypage";
    }

    @DeleteMapping("/users")
    public String delete(UserSessionInfo userSessionInfo, HttpServletRequest request) {
        userService.deleteUser(userSessionInfo.getEmail());
        LoginSessionManager loginSessionManager = new LoginSessionManager(request);
        loginSessionManager.clearSession();
        return "redirect:/";
    }
}
