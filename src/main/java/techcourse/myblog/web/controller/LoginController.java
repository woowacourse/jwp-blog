package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.UserReadService;
import techcourse.myblog.web.exception.LoginBindException;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;

@Controller
public class LoginController {
    private final UserReadService userReadService;

    public LoginController(UserReadService userReadService) {
        this.userReadService = userReadService;
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
                              @Validated(Default.class) UserDto userDto,
                              BindingResult bindingResult) throws LoginBindException {
        if (bindingResult.hasErrors()) {
            throw new LoginBindException(bindingResult);
        }

        User loginUser = userReadService.login(userDto);
        session.setAttribute("user", loginUser);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }
}