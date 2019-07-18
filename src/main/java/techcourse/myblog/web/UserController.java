package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;

import static techcourse.myblog.domain.User.*;
import static techcourse.myblog.web.UserController.USER_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(USER_MAPPING_URL)
public class UserController {
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
    public RedirectView signUp(User user){
        userService.save(user);
        return new RedirectView(USER_MAPPING_URL);
    }

}
