package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.service.UserWriteService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.validation.UserGroups.All;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserWriteService userWriteService;
    private final UserReadService userReadService;

    public UserController(final UserWriteService userWriteService,
                          final UserReadService userReadService) {
        this.userWriteService = userWriteService;
        this.userReadService = userReadService;
    }

    @GetMapping
    public String findAllUsersForm(Model model) {
        model.addAttribute("users", userReadService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("user");
        return new RedirectView("/");
    }

    @PostMapping
    public RedirectView createUser(@ModelAttribute("/signup") @Validated({All.class}) UserDto userDto) {
        userWriteService.save(userDto);
        return new RedirectView("/login");
    }

    @PostMapping("/login")
    public RedirectView login(HttpServletRequest httpServletRequest,
                              @ModelAttribute("/login") @Validated(Default.class) UserDto userDto) {
        Optional<User> user = userReadService.findByEmailAndPassword(userDto);

        if (user.isPresent()) {
            httpServletRequest.getSession().setAttribute("user", user.get());
            return new RedirectView("/");
        }

        throw new LoginFailException("이메일이나 비밀번호가 올바르지 않습니다.");
    }
}
