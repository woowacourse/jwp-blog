package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserSignUpRequestDto;
import techcourse.myblog.dto.UserUpdateRequestDto;
import techcourse.myblog.exception.NoUserException;
import techcourse.myblog.exception.SignUpFailException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String NO_USER_MESSAGE = "존재하지 않는 유저입니다.";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserSignUpRequestDto userSignUpRequestDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "signup";
        }

        try {
            userService.create(userSignUpRequestDto);
            return "redirect:/login";
        } catch (SignUpFailException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage() {
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "mypage-edit";
        }

        try {
            String email = ((User) httpSession.getAttribute("user")).getEmail();
            User user = userService.update(userUpdateRequestDto, email);
            httpSession.setAttribute("user", user);
            return "redirect:/users/mypage";
        } catch (NoUserException e) {
            log.error(NO_USER_MESSAGE);
            return "redirect:/";
        }
    }

    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        try {
            String email = ((User) httpSession.getAttribute("user")).getEmail();
            userService.delete(email);
            httpSession.removeAttribute("user");
        } catch (NoUserException e) {
            log.error(NO_USER_MESSAGE);
        }
        return "redirect:/";
    }
}
