package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.domain.User.UserRequestDto;
import techcourse.myblog.domain.User.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class UserController {
    public static final String EMAIL_DUPLICATION_ERROR_MSG = "이메일 중복입니다.";
    public static final String LOGIN_ERROR_MSG = "아이디나 비밀번호가 잘못되었습니다.";
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/accounts/signup")
    public String showSignupPage(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("user") != null) {
            return "redirect:/";
        }

        model.addAttribute("userRequestDto", new UserRequestDto());
        return "signup";
    }

    @PostMapping("/accounts/users")
    public String processSignup(@Valid UserRequestDto userRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            return "signup";
        }

        User user = userRequestDto.toUser();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "0", EMAIL_DUPLICATION_ERROR_MSG);
            return "signup";
        }

        userRepository.save(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(UserRequestDto userRequestDto, HttpServletRequest request, Model model) {
        Optional<User> user = userRepository.findByEmail(userRequestDto.getEmail());
        if (!user.isPresent() || !userRequestDto.getPassword().equals(user.get().getPassword())) {
            model.addAttribute("error", LOGIN_ERROR_MSG);
            return "login";
        }

        request.getSession().setAttribute("user", user.get());
        return "redirect:" + request.getHeader("Referer");

    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
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
        model.addAttribute("userRequestDto", new UserRequestDto((User) session.getAttribute("user")));
        return "mypage-edit";
    }

    @PutMapping("/accounts/profile/edit")
    public String processUpdateProfile(Model model, @Valid UserRequestDto userRequestDto, Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "mypage-edit";
        }
        userRepository.save(userRequestDto.toUser());
        request.getSession().setAttribute("user", userRequestDto.toUser());

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
        User user = (User) httpSession.getAttribute("user");
        userRepository.deleteById(user.getId());
        httpSession.removeAttribute("user");
        return "redirect:/";
    }
}
