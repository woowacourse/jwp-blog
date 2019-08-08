package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;
import techcourse.myblog.validation.UserInfo;
import techcourse.myblog.web.exception.EditUserBindException;
import techcourse.myblog.web.exception.SignUpBindException;
import techcourse.myblog.web.exception.UnauthorizedRequestException;
import techcourse.myblog.web.support.SessionUser;

import javax.validation.groups.Default;

@Controller
public class UserController {
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
    public RedirectView createUser(@Validated({Default.class, UserInfo.class}) UserDto userDto,
                                   BindingResult bindingResult) throws SignUpBindException {
        if (bindingResult.hasErrors()) {
            throw new SignUpBindException(bindingResult);
        }

        userWriteService.save(userDto.toUser());
        return new RedirectView("/login");
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

    @PutMapping("/users/{userId}")
    public RedirectView editUser(SessionUser loginUser,
                                 @PathVariable Long userId,
                                 @Validated(UserInfo.class) UserDto userDto,
                                 BindingResult bindingResult) throws EditUserBindException {
        checkAuthorization(loginUser, userId);

        if (bindingResult.hasErrors()) {
            throw new EditUserBindException(bindingResult);
        }

        userWriteService.update(loginUser.getUser(), userDto);
        return new RedirectView("/mypage");
    }

    @DeleteMapping("/users/{userId}")
    public RedirectView removeUser(SessionUser loginUser, @PathVariable Long userId) {
        checkAuthorization(loginUser, userId);

        userWriteService.remove(loginUser.getUser());
        return new RedirectView("/logout");
    }

    private void checkAuthorization(SessionUser loginUser, @PathVariable Long userId) {
        if (!loginUser.matchId(userId)) {
            throw new UnauthorizedRequestException();
        }
    }
}