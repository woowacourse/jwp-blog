package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateUserException;
import techcourse.myblog.exception.UnknownHostAccessException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignup(HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/users")
    public String enrollUser(@Valid UserDto userDto, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        userService.createUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession httpSession) {
        String name = (String) httpSession.getAttribute("name");

        if (name != null) {
            model.addAttribute("users", userService.getAllUsers());
            return "user-list";
        }

        throw new UnknownHostAccessException();
    }

    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");

        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", UserAssembler.writeDto(user));

            return "mypage";
        }

        throw new UnknownHostAccessException();
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");

        if (email != null) {
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", UserAssembler.writeDto(user));

            return "mypage-edit";
        }

        throw new UnknownHostAccessException();
    }

    @PutMapping("/mypage/edit")
    public String updateUser(@Valid UserDto userDto, HttpSession httpSession, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        User user = userService.updateUser(userDto);
        httpSession.setAttribute("name", user.getName());
        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage/edit/{userId}")
    public String deleteUser(@PathVariable long userId, HttpSession httpSession) {
        if (httpSession.getAttribute("name") == null) {
            throw new UnknownHostAccessException();
        }

        userService.deleteUser(userId);
        return "redirect:/logout";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, Model model) {
        model.addAttribute("errorMessage", e.getFieldError().getDefaultMessage());
        return "redirect:/signup";
    }

    @ExceptionHandler(DuplicateUserException.class)
    public String handleDuplicateEmailException(DuplicateUserException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/signup";
    }

    @ExceptionHandler(UnknownHostAccessException.class)
    public String handleUnknownHostAccessException(UnknownHostAccessException e, Model model) {
        model.addAttribute("unexpectedAccessErrorMessage", e.getMessage());
        return "redirect:/login";
    }
}
