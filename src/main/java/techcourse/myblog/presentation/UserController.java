package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.UserRepository;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class UserController {
    public static final String EMAIL_DUPLICATION_ERROR_MSG = "이메일 중복입니다.";
    public static final String LOGIN_ERROR_MSG = "아이디나 비밀번호가 잘못되었습니다.";
    private final UserService userService;
    private final UserRepository userRepository;

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
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "signup";
        }

        User user = userRequestDto.toUser();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", EMAIL_DUPLICATION_ERROR_MSG);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "signup";
        }

        userRepository.save(user);
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
        model.addAttribute("userRequestDto", new UserRequestDto((User) session.getAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY)));
        return "mypage-edit";
    }

    @PutMapping("/accounts/profile/edit")
    public String processUpdateProfile(@Valid UserRequestDto userRequestDto, Errors errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "mypage-edit";
        }
        User updatedUser = userService.update(userRequestDto.getEmail(), userRequestDto);
        request.getSession().setAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY, updatedUser);

        return "redirect:/accounts/profile/" + updatedUser.getId();
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "user-list";
    }

    @DeleteMapping("/accounts/delete")
    public RedirectView processDelete(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY);
        userRepository.deleteById(user.getId());
        httpSession.removeAttribute(LoginService.LOGGED_IN_USER_SESSION_KEY);
        
        return new RedirectView("/");
    }
}
