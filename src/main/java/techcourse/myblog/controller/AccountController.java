package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.AccountService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.controller.AccountController.ACCOUNT_URL;

@Slf4j
@Controller
@RequestMapping(ACCOUNT_URL)
public class AccountController {
    private final AccountService accountService;
    public static final String ACCOUNT_URL = "/accounts";

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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
        if (accountService.isExistsByEmail(user.getEmail())) {
            errors.rejectValue("email", "0", "이메일 중복입니다.");
            return "signup";
        }

        accountService.save(user);
        return "redirect:/";
    }

    @DeleteMapping("user")
    public String deleteUser(HttpSession session, User user) {
        log.debug(">>> deleteUser: request : {}", session);
        session.removeAttribute("user");
        accountService.delete(user);
        return "redirect:/";
    }

    @GetMapping("users")
    public String showUserList(Model model) {
        List<User> users = accountService.findAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        User user = accountService.findById(id);
        model.addAttribute("user", user);

        return "mypage";
    }

    @GetMapping("profile/edit")
    public String showProfileEditPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "mypage-edit";
    }

    @PutMapping("profile/edit")
    public String processUpdateProfile(@Valid UserDto userDto, Errors errors, User user, HttpSession session) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        if (user.isNotMatch(userDto)) {
            return "redirect:/";
        }

        session.setAttribute("user", accountService.update(userDto, user));

        return "redirect:/accounts/profile/" + userDto.getId();
    }
}
