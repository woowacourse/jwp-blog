package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.controller.dto.LoginDTO;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.exception.EmailRepetitionException;
import techcourse.myblog.exception.LoginFailException;
import techcourse.myblog.exception.UserNotExistException;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String loginForm(String loginError, Model model) {
        model.addAttribute("loginError", loginError);
        return "login";
    }

    @GetMapping("/signup")
    public String signUpForm(String signUpStatus, Model model) {
        model.addAttribute("signUpStatus", signUpStatus);
        return "signup";
    }

    @PostMapping
    public String create(@ModelAttribute UserDTO userDTO, RedirectAttributes redirectAttributes) {
        try {
            userService.save(userDTO);
            return "redirect:/users/login";
        } catch (EmailRepetitionException e) {
            log.error(e.getMessage());
            redirectAttributes.addAttribute("signUpStatus", e.getMessage());
            return "redirect:/users/signup";
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO loginDTO, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getLoginUser(loginDTO);
            log.info("userName : {}", user.getUserName());
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return "redirect:/";
        } catch (UserNotExistException | LoginFailException e) {
            log.error(e.getMessage());
            redirectAttributes.addAttribute("loginError", e.getMessage());
            return "redirect:/users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "user-list";
    }

    @DeleteMapping
    public String deleteUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        userService.delete(user.getEmail());
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String myPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateProfile(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        User user = userService.update(userDTO);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/users/mypage";
    }

    @GetMapping("/mypage-edit")
    public String myPageEdit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage-edit";
    }
}
