package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserRequestSignUpDto;
import techcourse.myblog.service.dto.UserRequestUpdateDto;
import techcourse.myblog.service.dto.UserSessionDto;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private LoginChecker loginChecker;

    public UserController(UserService userService, LoginChecker loginChecker) {
        this.userService = userService;
        this.loginChecker = loginChecker;
    }

    @GetMapping("/sign-up")
    public String showRegisterPage(HttpSession session) {
        if (loginChecker.isLoggedIn(session)) {
            return "redirect:/";
        }
        return "sign-up";
    }

    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping
    public String createUser(UserRequestSignUpDto userRequestSignUpDto) {
        userService.save(userRequestSignUpDto);
        return "redirect:/login";
    }

    @PutMapping("/{id}")
    public String editUserName(@PathVariable Long id, UserRequestUpdateDto userRequestUpdateDto, HttpSession session,
                               UserSessionDto userSessionDto) {
        if (loginChecker.isLoggedInSameId(session, id)) {
            userService.update(id, userRequestUpdateDto);
            userSessionDto.setName(userRequestUpdateDto.getName());
            session.setAttribute(LoginChecker.LOGGED_IN_USER, userSessionDto);
        }
        return "redirect:/mypage/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (loginChecker.isLoggedInSameId(session, id)) {
            userService.delete(id);
            session.removeAttribute(LoginChecker.LOGGED_IN_USER);
        }
        return "redirect:/";
    }
}
