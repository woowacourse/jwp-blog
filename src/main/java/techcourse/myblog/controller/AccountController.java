package techcourse.myblog.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.controller.AccountController.ACCOUNT_URL;

@Slf4j
@Controller
@RequestMapping(ACCOUNT_URL)
public class AccountController {
    public static final String ACCOUNT_URL = "/accounts";
    private AccountService accountService;

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

        if (accountService.isExistUser(userDto)) {
            errors.rejectValue("email", "0", "이메일 중복입니다.");
            return "signup";
        }

        accountService.signUp(userDto);
        return "redirect:/";
    }

    @DeleteMapping("user")
    public String deleteUser(HttpSession httpSession) {
        User user  = (User) httpSession.getAttribute("user");
        httpSession.removeAttribute("user");
        accountService.deleteUser(user.getId());
        return "redirect:/";
    }

    @GetMapping("users")
    public String showUserList(Model model) {
        List<User> users = accountService.getUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        User user = accountService.findUserOrElseThrow(id);
        model.addAttribute("user", user);

        return "mypage";
    }

    @GetMapping("profile/edit")
    public String showProfileEditPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "mypage-edit";
    }

    @PutMapping("profile/edit")
    public String processUpdateProfile(HttpSession httpSession, @Valid UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        User user = accountService.saveUser(userDto);
        httpSession.setAttribute("user", user);

        return "redirect:/accounts/profile/" + user.getId();
    }
}
