package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.repo.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "signup";
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            model.addAttribute("error", "중복된 이메일 입니다.");
            return "signup";
        }

        User user = new User(userDto);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage-edit";
    }

    @Transactional
    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "mypage-edit";
        }

        String email = ((User) httpSession.getAttribute("user")).getEmail();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            user.setUserName(userUpdateRequestDto.getUserName());
            httpSession.setAttribute("user", user);
            return "redirect:/users/mypage";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }

    @Transactional
    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            httpSession.removeAttribute("user");
            userRepository.delete(user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }
}
