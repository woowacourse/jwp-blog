package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.dto.LoginUserDto;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/sign-up")
    public String showRegisterPage(HttpSession httpSession) {
        if (isLoggedIn(httpSession)) {
            return "redirect:/";
        }
        return "sign-up";
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PostMapping("/users")
    public String createUser(UserDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }

    @PutMapping("/users/{id}")
    public String editUserName(@PathVariable Long id, UserPublicInfoDto userPublicInfoDto,
                               LoginUserDto user, HttpSession httpSession) {
        userService.update(userPublicInfoDto, id, user.getId());
        user.setName(userPublicInfoDto.getName());
        httpSession.setAttribute(LOGGED_IN_USER, user);
        return "redirect:/mypage/" + id;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id, LoginUserDto user, HttpSession httpSession) {
        userService.delete(id, user.getId());
        httpSession.removeAttribute(LOGGED_IN_USER);
        return "redirect:/";
    }

    private boolean isLoggedIn(HttpSession httpSession) {
        return httpSession.getAttribute(LOGGED_IN_USER) != null;
    }
}
