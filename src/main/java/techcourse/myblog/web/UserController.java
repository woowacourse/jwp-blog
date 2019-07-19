package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static techcourse.myblog.domain.User.*;
import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
public class UserController {
    public static final String USER_MAPPING_URL = "/user";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public final UserService userService;

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("emailPattern", EMAIL_PATTERN);
        model.addAttribute("namePattern", NAME_PATTERN);
        model.addAttribute("passwordPattern", PASSWORD_PATTERN);
        return "signup";
    }

    @PostMapping
    public RedirectView signUp(UserDto userDto) {
        userService.save(userDto);
        return new RedirectView(USER_MAPPING_URL);
    }

    @GetMapping("/show")
    public String show(UserDto userDto, Model model) {
        log.info(userDto.getName() + " " + userDto.getEmail());
        model.addAttribute("userDto", userDto);
        return "mypage";
    }

    @GetMapping("/edit")
    public String edit(UserDto userDto, Model model) {
        log.info(userDto.getName() + " " + userDto.getEmail());
        model.addAttribute("userDto", userDto);
        return "mypage-edit";
    }

    @PostMapping("/login")
    public RedirectView login(UserDto userDto, HttpServletRequest request) {
        log.info(request.getSession().getId());
        log.info(userDto.getName() + " " + userDto.getEmail() + " " + userDto.getPassword());
        if (request.getAttribute("login") != null) {
            request.removeAttribute("login");
        }

        if (userService.isMatchPassword(userDto)) {
            request.setAttribute("user", userDto);
            return new RedirectView("/");
        }
        return new RedirectView("/user");
    }
}
