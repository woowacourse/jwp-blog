package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.InvalidUserDataException;
import techcourse.myblog.service.UserService;

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

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession httpSession) {
        User user = userService.getUser(userDto);
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("email", user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("name");
        httpSession.removeAttribute("email");
        return "redirect:/";
    }

    @PostMapping("/users")
    public String enrollUser(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");

        if (email != null) {
            User user = userService.getUser(email);
            model.addAttribute("user", UserAssembler.writeDto(user));

            return "mypage";
        }

        return "redirect:/login";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");

        if (email != null) {
            User user = userService.getUser(email);
            model.addAttribute("user", UserAssembler.writeDto(user));

            return "mypage-edit";
        }

        return "redirect:/login";
    }

    @PutMapping("/mypage/edit")
    public String updateUser(UserDto userDto, HttpSession httpSession) {
        userService.updateUser(userDto);
        httpSession.setAttribute("name", userDto.getName());
        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage/delete")
    public String deleteUser(HttpSession httpSession) {
        userService.deleteUser((String) httpSession.getAttribute("email"));
        return "redirect:/logout";
    }

    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidUserDataException(InvalidUserDataException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
