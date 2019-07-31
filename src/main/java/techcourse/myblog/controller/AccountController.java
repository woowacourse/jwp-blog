package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.controller.AccountController.ACCOUNT_URL;

@Slf4j
@Controller
@RequestMapping(ACCOUNT_URL)
public class AccountController {
    private UserRepository userRepository;
    public static final String ACCOUNT_URL = "/accounts";

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "signup";
    }

    @PostMapping("user")
    public String processSignup(@Valid UserDto userDto, Errors errors) {
        log.debug(">>> UserDto : {} Error : {}", userDto, errors);
        if (errors.hasErrors()) {
            return "signup";
        }

        User user = userDto.toUser();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", "이메일 중복입니다.");
            return "signup";
        }

        userRepository.save(user);

        return "redirect:/";
    }

    @DeleteMapping("user")
    public String deleteUser(HttpSession session, User user) {
        log.debug(">>> deleteUser: request : {}", session);
        session.removeAttribute("user");
        userRepository.delete(user);
        return "redirect:/";
    }

    @GetMapping("users")
    public String showUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        model.addAttribute("user", user);

        return "mypage";
    }

    @GetMapping("profile/edit")
    public String showProfileEditPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "mypage-edit";
    }

    @PutMapping("profile/edit")
    public String processUpdateProfile(@Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        userRepository.save(userDto.toUser());
        session.setAttribute("user", userDto.toUser());

        return "redirect:/accounts/profile/" + userDto.getId();
    }
}
