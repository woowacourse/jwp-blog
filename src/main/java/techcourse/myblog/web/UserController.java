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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

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

    @GetMapping(path = {"/{email}", "/{email}/edit"})
    public String showMyPageEdit(@PathVariable String email, HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
        String sessionEmail = (String) session.getAttribute("email");

        User authenticatedUser = userService.getAuthenticatedUser(email, sessionEmail);
        model.addAttribute("user", authenticatedUser);

        if (req.getRequestURI().contains("edit")) {
            return "mypage-edit";
        }
        return "mypage";
    }

    @PutMapping("/{email}")
    public RedirectView myPageEdit(@PathVariable String email, @Valid UserDto userDto, Errors errors, HttpSession session) {
        if (errors.hasErrors()) {
            throw new UpdateUserInputException("잘못된 입력값입니다.");
        }

        String sessionEmail = (String) session.getAttribute("email");
        User updatedUser = userService.update(userDto, email, sessionEmail);

        session.setAttribute("username", updatedUser.getName());
        return new RedirectView("/users/" + email);
    }

    @DeleteMapping("/{email}")
    public RedirectView exitUser(@PathVariable String email, HttpSession session) {
        String sessionEmail = (String) session.getAttribute("email");

        userService.exit(email, sessionEmail);
        session.invalidate();

        return new RedirectView("/");
    }

    @ExceptionHandler({UserNotFoundException.class, UserForbiddenException.class})
    public RedirectView userAuthException(RedirectAttributes redirectAttributes, Exception exception) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());

        return new RedirectView("/");
    }
}