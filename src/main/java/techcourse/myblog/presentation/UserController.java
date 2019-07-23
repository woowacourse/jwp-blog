package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserRequestDto;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

@Slf4j
@Controller
public class UserController {
    public static final String EMAIL_DUPLICATION_ERROR_MSG = "이메일 중복입니다.";
    public static final String LOGIN_ERROR_MSG = "아이디나 비밀번호가 잘못되었습니다.";
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/accounts/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        return "signup";
    }

    @PostMapping("/accounts/users")
    public String processSignup(@Valid UserRequestDto userRequestDto, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            response.setStatus(400);
            return "signup";
        }

        User user = userRequestDto.toUser();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", EMAIL_DUPLICATION_ERROR_MSG);
            response.setStatus(400);
            return "signup";
        }

        userRepository.save(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserRequestDto userRequestDto, HttpServletRequest request) {
        request.getSession().setAttribute(LOGGED_IN_USER_SESSION_KEY, userService.authenticate(userRequestDto));
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(LOGGED_IN_USER_SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping("/accounts/profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        model.addAttribute("user", user);

        return "mypage";
    }

    @GetMapping("/accounts/profile/edit")
    public String showProfileEditPage(Model model, HttpSession session) {
        model.addAttribute("userRequestDto", new UserRequestDto((User) session.getAttribute(LOGGED_IN_USER_SESSION_KEY)));
        return "mypage-edit";
    }

    @PutMapping("/accounts/profile/edit")
    public String processUpdateProfile(@Valid UserRequestDto userRequestDto, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        userService.update(userRequestDto.getEmail(), userRequestDto);
        request.getSession().setAttribute(LOGGED_IN_USER_SESSION_KEY, userRequestDto.toUser());

        return "redirect:/accounts/profile/" + userRequestDto.getId();
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @DeleteMapping("/accounts/delete")
    public String processDelete(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        userRepository.deleteById(user.getId());
        httpSession.removeAttribute(LOGGED_IN_USER_SESSION_KEY);
        return "redirect:/";
    }
}
