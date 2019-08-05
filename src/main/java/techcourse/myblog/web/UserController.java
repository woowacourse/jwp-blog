package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private String getEmailFromSession(HttpSession session) {
        return (String) session.getAttribute("email");
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("name");
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login-form";
    }

    @PostMapping("/login")
    public RedirectView login(String email, String password, HttpSession session) {
        userService.tryLogin(email, password, session);
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        clearSession(session);
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.loadEveryUsers());
        return "user-list";
    }

    @GetMapping("/signup")
    public String signupForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup-form";
    }

    @PostMapping("/users")
    public RedirectView signup(String name, String email, String password) {
        userService.tryRegister(name, email, password);
        return new RedirectView("/login");
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        model.addAttribute("user", userService.getUserByEmail(getEmailFromSession(session)));
        return "mypage";
    }

    @GetMapping("/profile/edit")
    public String profileEditForm(
            @RequestParam(required = false) String errorMessage,
            Model model,
            HttpSession session
    ) {
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("user", userService.getUserByEmail(getEmailFromSession(session)));
        return "mypage-edit";
    }

    @PutMapping("/profile/edit")
    public RedirectView profileEdit(String name, String email, HttpSession session) {
        userService.tryUpdate(name, email, getEmailFromSession(session));
        session.setAttribute("name", name);
        session.setAttribute("email", email);
        return new RedirectView("/profile");
    }

    @DeleteMapping("/profile")
    public RedirectView cancelProfile(HttpSession session) {
        userService.delete(getEmailFromSession(session));
        clearSession(session);
        return new RedirectView("/");
    }
}