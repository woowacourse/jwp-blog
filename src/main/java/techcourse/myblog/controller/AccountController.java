package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.UserDuplicateEmailException;
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

    @GetMapping("users")
    public String showUserList(Model model) {
        List<User> users = accountService.findAll();
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

    @PostMapping("user")
    public String processSignup(@Valid UserDto userDto, Errors errors) {
        log.debug(">>> UserDto : {} Error : {}", userDto, errors);
        if (errors.hasErrors()) {
            return "signup";
        }

        try {
            accountService.save(userDto);
        } catch (UserDuplicateEmailException e) {
            errors.rejectValue("email", "0", e.getMessage());
            return "signup";
        }

        return "redirect:/";
    }

    @PutMapping("profile/{id}")
    public String processUpdateProfile(@PathVariable long id, @Valid UserDto userDto, Errors errors, User user, HttpSession session) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }

        User updatedUser = accountService.update(id, userDto, user);
        session.setAttribute("user", updatedUser);

        return "redirect:/accounts/profile/" + id;
    }

    @DeleteMapping("user")
    public String deleteUser(HttpSession session, User user) {
        log.debug(">>> deleteUser: request : {}", session);
        session.removeAttribute("user");
        accountService.delete(user);
        return "redirect:/";
    }
}
