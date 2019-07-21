package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
import techcourse.myblog.exception.FailedLoginException;
import techcourse.myblog.exception.InvalidUserDataException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String enrollUser(UserDto userDto) {
        userService.createUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(UserDto userDto, HttpSession httpSession) {
        User user = userService.findUserByEmailAndPassword(userDto);
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("email", user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            httpSession.removeAttribute("name");
            httpSession.removeAttribute("email");
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String showUsers(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("name") != null) {
            model.addAttribute("users", userService.getAllUsers());
            return "user-list";
        }
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String showMypage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");
        if (email != null) {
            User user = userService.findUserByEmailAndPassword(email);
            model.addAttribute("user", UserAssembler.writeDto(user));
            return "mypage";
        }
        return "redirect:/login";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(Model model, HttpSession httpSession) {
        String email = (String) httpSession.getAttribute("email");
        if (email != null) {
            User user = userService.findUserByEmailAndPassword(email);
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

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateEmailException(DuplicateEmailException e, HttpServletRequest request, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(FailedLoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleFailedLoginException(FailedLoginException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "login";
    }
}
