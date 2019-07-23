package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserForm;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class AccountController {
    private UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/accounts/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "signup";
    }

    @PostMapping("/accounts/user")
    public String processSignup(@Valid UserForm userForm, Errors errors) {
        log.debug(">>> UserForm : {} Error : {}", userForm, errors);
        if (errors.hasErrors()) {
            return "signup";
        }

        User user = userForm.toUser();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", "이메일 중복입니다.");
            return "signup";
        }

        userRepository.save(user);

        return "redirect:/";
    }

    @DeleteMapping("/accounts/user")
    public String deleteUser(HttpServletRequest request) {
        log.debug(">>> deleteUser: request : {}", request);
        log.debug(">>> deleteUser: requestMethod : {}", request.getMethod());
        User user = (User) request.getSession().getAttribute("user");
        request.getSession().removeAttribute("user");
        userRepository.delete(user);
        return "redirect:/";
    }

    @GetMapping("/accounts/users")
    public String showUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/accounts/profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        model.addAttribute("user", user);

        return "mypage";
    }

    @GetMapping("/accounts/profile/edit")
    public String showProfileEditPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "mypage-edit";
    }

    @PutMapping("/accounts/profile/edit")
    public String processUpdateProfile(HttpServletRequest request, @Valid UserForm userForm, Errors errors) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        userRepository.save(userForm.toUser());
        request.getSession().setAttribute("user", userForm.toUser());

        return "redirect:/accounts/profile/" + userForm.getId();
    }
}
