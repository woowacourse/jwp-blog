package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserRequestDto;
import techcourse.myblog.service.UserService;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @PostMapping("/users")
    public ModelAndView registerUsers(@Valid UserRequestDto userRequestDto) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("login"));
        userService.save(userRequestDto);
        return modelAndView;
    }
}
