package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import javax.validation.groups.Default;

import lombok.AllArgsConstructor;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserInfo;
import techcourse.myblog.controller.session.UserSession;
import techcourse.myblog.controller.session.UserSessionManager;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserSessionManager userSessionManager;

    @GetMapping("/signup")
    public String createSignupForm(Model model) {
        bindingModelData(model);
        return "signup";
    }

    private void bindingModelData(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }
    }

    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView save(@ModelAttribute("/signup") @Validated({Default.class, UserInfo.class}) UserDto userDto) {
        userService.save(userDto.toDomain());
        return new RedirectView("/login");
    }

    @DeleteMapping("/users")
    public RedirectView delete() {
        userService.delete(userSessionManager.getUser());
        return logout();
    }

    @GetMapping("/login")
    public String createLoginForm(Model model) {
        bindingModelData(model);
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute("/login") UserDto userDto) {
        userSessionManager.setUser(userService.login(userDto.toDomain()));
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        userSessionManager.removeUser();
        return new RedirectView("/");
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String createMyPageForm(Model model) {
        bindingModelData(model);
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView update(@ModelAttribute("/mypage/edit") @Validated(UserInfo.class) UserDto userDto,
                               UserSession userSession) {
        User user = userService.update(userSession.getUser(), userDto.getName());
        userSessionManager.setUser(user);
        return new RedirectView("/mypage");
    }
}
