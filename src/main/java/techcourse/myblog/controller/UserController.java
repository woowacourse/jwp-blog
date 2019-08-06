package techcourse.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.FailedPasswordVerificationException;
import techcourse.myblog.exception.InvalidUserDataException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.web.LoginUser;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/users/new")
    public String enrollUser(UserDto userDto) {
        userService.save(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession httpSession) {
        User user = userService.findUserByEmailAndPassword(userDto);
        httpSession.setAttribute("loginUser", new LoginUser(user.getName(), user.getEmail()));
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if (httpSession.getAttribute("loginUser") != null) {
            httpSession.removeAttribute("loginUser");
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession httpSession) {
        String email = ((LoginUser) httpSession.getAttribute("loginUser")).getEmail();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", UserAssembler.writeDto(user));
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(Model model, HttpSession httpSession) {
        String email = ((LoginUser) httpSession.getAttribute("loginUser")).getEmail();
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", UserAssembler.writeDto(user));
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String updateUser(UserDto userDto, HttpSession httpSession) {
        User updatedUser = userService.update(userDto);
        httpSession.setAttribute("loginUser", new LoginUser(updatedUser.getName(), updatedUser.getEmail()));
        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage/delete")
    public String deleteUser(HttpSession httpSession) {
        String email = ((LoginUser) httpSession.getAttribute("loginUser")).getEmail();
        userService.deleteByEmail(email);
        return "redirect:/logout";
    }

    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidUserDataException(InvalidUserDataException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateEmailException(DuplicateEmailException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(FailedLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedLoginException(FailedLoginException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }

    @ExceptionHandler(FailedPasswordVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedPasswordVerificationException(FailedPasswordVerificationException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
