package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.*;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.web.UserController.USER_DEFAULT_URL;

@Controller
@RequestMapping(USER_DEFAULT_URL)
public class UserController {
    public static final String USER_DEFAULT_URL = "/users";

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.fetchAllUsers());
        return "user-list";
    }

    @GetMapping("/signup")
    public String showSignupForm(@ModelAttribute String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "signup";
    }

    @PostMapping
    public RedirectView registerUser(@Valid UserDto userDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new SignUpInputException("회원 가입에 필요한 값이 잘못됐습니다. 확인해주세요");
        }

        userService.register(userDto);
        return new RedirectView("/auth/login");
    }

    @ExceptionHandler({SignUpInputException.class, AlreadyExistUserException.class})
    public RedirectView registerException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return new RedirectView("/users/signup");
    }

    @GetMapping("/{email}")
    public String showMyPage(@PathVariable String email, HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("user");

        UserDto userInfo = userService.getUserInfo(email, user.getEmail());
        model.addAttribute("user", userInfo);

        return "mypage";
    }

    @GetMapping("/{email}/edit")
    public String showMyInfoEdit(@PathVariable String email, HttpSession session, Model model) {
        UserDto user = (UserDto) session.getAttribute("user");

        UserDto userInfo = userService.getUserInfo(email, user.getEmail());
        model.addAttribute("user", userInfo);

        return "mypage-edit";
    }

    @PutMapping("/{email}")
    public RedirectView myPageEdit(@PathVariable String email, @Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UpdateUserInputException("잘못된 입력값입니다.");
        }

        UserDto user = (UserDto) session.getAttribute("user");
        UserDto updatedUser = userService.update(userDto, email, user.getEmail());

        session.setAttribute("username", updatedUser.getName());
        return new RedirectView("/users/" + email);
    }

    @DeleteMapping("/{email}")
    public RedirectView exitUser(@PathVariable String email, HttpSession session) {
        UserDto user = (UserDto) session.getAttribute("user");

        userService.exit(email, user.getEmail());
        session.invalidate();

        return new RedirectView("/");
    }

    @ExceptionHandler({UserNotFoundException.class, UserForbiddenException.class})
    public RedirectView userAuthException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/");
    }
}