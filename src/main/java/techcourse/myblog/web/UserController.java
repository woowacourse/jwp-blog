package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserAssembler;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicateEmailException;
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

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "redirect:/signup";
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateEmailException(DuplicateEmailException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";
    }
}
