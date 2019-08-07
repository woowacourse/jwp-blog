package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.domain.UserDTO;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String createForm(String signUpStatus, Model model) {
        model.addAttribute("signUpStatus", signUpStatus);
        return "signup";
    }

    @PostMapping
    public RedirectView create(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.save(userDTO);
            return new RedirectView("/login");
        } catch (EmailRepetitionException e) {
            log.error(e.getMessage());
            redirectAttributes.addAttribute("signUpStatus", e.getMessage());
            return new RedirectView("/users/signup");
        }
    }

    @GetMapping
    public String show(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }

    @DeleteMapping
    public RedirectView delete(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user);
        session.invalidate();
        return new RedirectView("/");
    }
}
