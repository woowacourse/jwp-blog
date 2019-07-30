package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.UserInfo;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;
import techcourse.myblog.web.LoginUser;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;
import java.util.Optional;

@Controller
public class UserController {
    public static final String LOGIN_FAIL_MESSAGE = "이메일이나 비밀번호가 올바르지 않습니다";
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    public UserController(UserReadService userReadService, UserWriteService userWriteService) {
        this.userReadService = userReadService;
        this.userWriteService = userWriteService;
    }

    @GetMapping("/signup")
    public String createSignupForm(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }
        return "signup";
    }

    @PostMapping("/users")
    public RedirectView createUser(@ModelAttribute("/signup") @Validated({Default.class, UserInfo.class}) UserDto userDto) {
        userWriteService.save(userDto);

        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String createLoginForm(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }

        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(HttpSession session,
                              @ModelAttribute("/login") @Validated(Default.class) UserDto userDto) {
        Optional<User> loginUser = userReadService.findByEmailAndPassword(userDto);

        if (loginUser.isPresent()) {
            session.setAttribute("user", loginUser.get());
            return new RedirectView("/");
        }

        throw new LoginFailedException(LOGIN_FAIL_MESSAGE);
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userReadService.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String createMyPageForm(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView editUser(LoginUser loginUser,
                                 @ModelAttribute("/mypage/edit") @Validated(UserInfo.class) UserDto userDto) {
        userWriteService.update(loginUser.getUser(), userDto);

        return new RedirectView("/mypage");
    }

    @DeleteMapping("/mypage")
    public RedirectView removeUser(LoginUser loginUser) {
        userWriteService.remove(loginUser.getUser());

        return new RedirectView("/logout");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("userDto", new UserDto("", "", ""));
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDto", e.getBindingResult());
        return new RedirectView(e.getObjectName());
    }
}