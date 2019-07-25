package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.exception.NotValidUpdateUserInfoException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.dto.UserUpdateRequestDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) throws NotValidUserInfoException {
        if (bindingResult.hasErrors()) {
            throw new NotValidUserInfoException(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.createNewUser(userDto);
        return "redirect:/login";
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession) {
        userService.checkRequestAboutMypage(httpSession);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(HttpSession httpSession) {
        userService.checkRequestAboutMypage(httpSession);
        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto,
                               BindingResult bindingResult, HttpSession httpSession) {
        User user = userService.updateUser(bindingResult, httpSession, userUpdateRequestDto);
        httpSession.setAttribute("user", user);
        return "redirect:/users/mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        userService.deleteUser(httpSession);
        httpSession.removeAttribute("user");
        return "redirect:/";
    }

    @ExceptionHandler(NotValidUserInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotValidUpdateInformation(NotValidUserInfoException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "signup";
    }

    @ExceptionHandler(NotValidUpdateUserInfoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUpdateUserException(NotValidUpdateUserInfoException e, Model model) {
        log.error(e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "mypage-edit";
    }

}
