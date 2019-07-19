package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;

import static techcourse.myblog.domain.User.*;
import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public static final String USER_MAPPING_URL ="/user";
    public final UserService userService;

    @GetMapping
    public String loginForm(){
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model){
        model.addAttribute("emailPattern",EMAIL_PATTERN);
        model.addAttribute("namePattern",NAME_PATTERN);
        model.addAttribute("passwordPattern",PASSWORD_PATTERN);
        return "signup";
    }

    @PostMapping
    public RedirectView signUp(UserDto userDto){
        userService.save(userDto);
        return new RedirectView(USER_MAPPING_URL);
    }

    @GetMapping("/show")
    public String show(@AuthenticationPrincipal User user, Model model){
        log.info(user.getName() + " "+ user.getEmail());
        model.addAttribute("user",user);
        return "mypage";
    }

    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal User user, Model model){
        log.info(user.getName() + " " + user.getEmail());
        model.addAttribute("user",user);
        return "mypage-edit";
    }

}
