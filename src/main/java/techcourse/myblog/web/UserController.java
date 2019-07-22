package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.UserInfo;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;
import java.util.Optional;

@Controller
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String createSignupForm(UserDto userDto, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "signup";
    }

    @GetMapping("/login")
    public String createLoginForm(UserDto userDto, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String createMyPageForm(UserDto userDto, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public String editUser(@Validated(UserInfo.class) UserDto userDto, BindingResult bindingResult,
                           HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "mypage-edit";
        }

        user.modifyName(userDto.getName());
        userRepository.save(user);
        return "redirect:/mypage";
    }

    @PostMapping("/users")
    public String createUser(@Validated({Default.class, UserInfo.class}) UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            userRepository.save(userDto.toUser());
        } catch (DataIntegrityViolationException e) {
            bindingResult.addError(new FieldError("userDto", "email", "이미 존재하는 email입니다."));
            return "signup";
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession session, BindingResult bindingResult) {
        Optional<User> loginUser = userRepository.findByEmail(userDto.getEmail());

        if (!loginUser.isPresent()) {
            bindingResult.addError(new FieldError("userDto", "email", "이메일을 확인해주세요."));
            return "login";
        }

        if (!loginUser.get().matchPassword(userDto.toUser())) {
            bindingResult.addError(new FieldError("userDto", "password", "비밀번호를 확인해주세요."));
            return "login";
        }

        session.setAttribute("user", loginUser.get());
        return "redirect:/";
    }

    @DeleteMapping("/users")
    public RedirectView removeUser(UserDto userDto, HttpSession session) { //탈퇴
        User user = (User) session.getAttribute("user");
        if (user != null && user.matchEmail(user)) {
            userRepository.delete(user);
        }
        session.invalidate();
        return new RedirectView("/");
    }
}
