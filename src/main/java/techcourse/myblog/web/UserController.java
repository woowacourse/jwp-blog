package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserAuthenticateException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.dto.LoginRequestDto;
import techcourse.myblog.web.dto.UserRequestDto;
import techcourse.myblog.web.dto.UserUpdateRequestDto;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static techcourse.myblog.web.ControllerUtil.*;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final Validator validator;

    @Autowired
    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping("/signup")
    public String registerView(HttpSession session) {
        if (isLoggedIn(session)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/users")
    public String register(UserRequestDto userRequestDto, Model model, @SessionAttribute(SESSION_USER_KEY) Optional<User> currentUser) {
        try {
            assertValidRequest(validator.validate(userRequestDto));
            if (currentUser.isPresent()) {
                return "redirect:/";
            }
            userService.register(userRequestDto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "signup";
        }
    }

    private <T> void assertValidRequest(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }

    @GetMapping("/login")
    public String loginView(@SessionAttribute(SESSION_USER_KEY) Optional<User> currentUser) {
        if (currentUser.isPresent()) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginRequestDto requestDto, Model model, HttpSession session) {
        try {
            if (isLoggedIn(session)) {
                return "redirect:/";
            }
            User user = userService.authenticate(requestDto);
            session.setAttribute(SESSION_USER_KEY, user);
            return "redirect:/";
        } catch (UserAuthenticateException e) {
            logger.info("Login failed: {}", e.getMessage());
            model.addAttribute("message", e.getMessage());
        } catch (Exception e) {
            logger.error("Login error", e);
            model.addAttribute("message", "서버 에러입니다");
        }
        model.addAttribute("error", true);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String userListView(Model model, @SessionAttribute(SESSION_USER_KEY) Optional<User> currentUser) {
        checkAndPutUser(model, currentUser);
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
    public String myPageView(Model model, HttpSession session) {
        if (isLoggedIn(session)) {
            model.addAttribute(SESSION_USER_KEY, session.getAttribute(SESSION_USER_KEY));
            return "mypage";
        }
        return "redirect:/login";
    }

    @GetMapping("/mypage-edit")
    public String myPageEditView(Model model, @SessionAttribute(SESSION_USER_KEY) Optional<User> currentUser) {
        if (currentUser.isPresent()) {
            model.addAttribute(SESSION_USER_KEY, currentUser.get());
            return "mypage-edit";
        }
        return "redirect:login";
    }

    @PutMapping("/users")
    public String updateUser(UserUpdateRequestDto updateRequestDto, HttpSession session, Model model) {
        try {
            assertValidRequest(validator.validate(updateRequestDto));
            if (!isLoggedIn(session)) {
                return "redirect:/login";
            }
            User user = (User) session.getAttribute(SESSION_USER_KEY);
            User updated = userService.update(user.getId(), updateRequestDto);
            session.setAttribute(SESSION_USER_KEY, updated);
            return "redirect:/mypage";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", true);
            model.addAttribute("message", e.getMessage());
            return "mypage";
        }
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
