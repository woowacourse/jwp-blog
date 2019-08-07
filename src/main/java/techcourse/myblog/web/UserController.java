package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.exception.AlreadyLoggedInException;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private UserService userService;
    private LoginChecker loginChecker;

    public UserController(UserService userService, LoginChecker loginChecker) {
        this.userService = userService;
        this.loginChecker = loginChecker;
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage(HttpSession session) {
        if (loginChecker.isLoggedIn(session)) {
            throw new AlreadyLoggedInException();
        }
        return "sign-up";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public String createUser(UserRequestDto userRequestDto) {
        userService.save(userRequestDto);
        return "redirect:/login";
    }

    @PutMapping("/users/{id}")
    public String editUserName(@PathVariable Long id, UserRequestDto userRequestDto, HttpSession session, UserSessionDto userSessionDto) {
        if (loginChecker.isLoggedInSameId(session, id)) {
            UserPublicInfoDto updateResult = userService.update(id, userRequestDto);
            userSessionDto.setName(updateResult.getName());
            session.setAttribute(LoginChecker.LOGGED_IN_USER, userSessionDto);
        }
        return "redirect:/mypage/" + id;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (loginChecker.isLoggedInSameId(session, id)) {
            userService.delete(id);
            session.removeAttribute(LoginChecker.LOGGED_IN_USER);
        }
        return "redirect:/";
    }
}
