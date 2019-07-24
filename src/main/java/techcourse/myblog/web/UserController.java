package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.dto.LoginRequestDto;
import techcourse.myblog.web.dto.UserRequestDto;
import techcourse.myblog.web.dto.UserUpdateRequestDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.web.LoginUtil.SESSION_USER_KEY;
import static techcourse.myblog.web.LoginUtil.isLoggedIn;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String registerView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/users")
    public String register(@Valid UserRequestDto userRequestDto, BindingResult bindingResult,
                           Model model,
                           @SessionAttribute(name = SESSION_USER_KEY, required = false) User currentUser) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "signup";
        }
        if (currentUser != null) {
            return "redirect:/";
        }
        userService.register(userRequestDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginView(@SessionAttribute(name = SESSION_USER_KEY, required = false) User currentUser) {
        if (currentUser != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginRequestDto requestDto, BindingResult bindingResult, Model model, HttpSession session) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "login";
        }
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        session.setAttribute(SESSION_USER_KEY, userService.findByEmail(requestDto.getEmail())
            .orElseThrow(null));
        return "redirect:/";
    }

    @GetMapping("/users")
    public String userListView(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (isLoggedIn(session)) {
            session.invalidate();
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String myPageView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "mypage";
        }
        return "redirect:/login";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditView(@SessionAttribute(name = SESSION_USER_KEY, required = false) User currentUser) {
        if (currentUser == null) {
            return "redirect:login";
        }
        return "mypage-edit";
    }

    @PutMapping("/users")
    public String updateUser(@Valid UserUpdateRequestDto updateRequestDto, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", true);
            model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "mypage";
        }
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        User user = (User) session.getAttribute(SESSION_USER_KEY);
        User updated = userService.update(user.getId(), updateRequestDto);
        session.setAttribute(SESSION_USER_KEY, updated);
        return "redirect:/mypage";
    }

    @DeleteMapping("/withdraw")
    public String deleteUser(HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        userService.delete(((User) session.getAttribute(SESSION_USER_KEY)).getId());
        session.invalidate();
        return "redirect:/";
    }
}
