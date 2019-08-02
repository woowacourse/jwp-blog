package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.LoginUser;
import techcourse.myblog.application.service.UserQueryResult;
import techcourse.myblog.application.service.UserService;

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
        session.removeAttribute("user");
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "login-form";
    }

    @PostMapping("/login")
    public RedirectView login(String email, String password, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            session.setAttribute("user", userService.tryLogin(email, password));
            return new RedirectView("/");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "존재하지 않는 사용자이거나 잘못된 비밀번호입니다."
            );
            return new RedirectView("/login");
        }
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
    public RedirectView signup(
            String name,
            String email,
            String password,
            RedirectAttributes redirectAttributes
    ) {
        final UserQueryResult result = userService.tryRegister(name, email, password);
        if (result == UserQueryResult.SUCCESS) {
            return new RedirectView("/login");
        }
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                (result == UserQueryResult.EMAIL_ALREADY_TAKEN) ? "이미 등록된 이메일입니다." : "잘못된 입력입니다."
        );
        return new RedirectView("/signup");
    }

    @GetMapping("/profile")
    public String profile(Model model, LoginUser loginUser) {
        model.addAttribute("user", loginUser.getUser());
        return "mypage";
    }

    @GetMapping("/profile/edit")
    public String profileEditForm(
            @RequestParam(required = false) String errorMessage,
            Model model,
            LoginUser loginUser
    ) {
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        model.addAttribute("user", loginUser.getUser());
        return "mypage-edit";
    }

    @PutMapping("/profile/edit")
    public RedirectView profileEdit(String name, LoginUser loginUser, HttpSession session) {
        session.setAttribute("user", userService.tryUpdate(name, loginUser.getUser()));
        return new RedirectView("/profile");
    }


    @DeleteMapping("/profile")
    public RedirectView cancelProfile(LoginUser loginUser, HttpSession session) {
        userService.delete(loginUser.getUser());
        clearSession(session);
        return new RedirectView("/");
    }
}