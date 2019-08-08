package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.exception.NotValidUpdateUserInfoException;
import techcourse.myblog.exception.NotValidUserInfoException;
import techcourse.myblog.service.UserService;
import techcourse.myblog.domain.dto.UserDto;
import techcourse.myblog.domain.dto.UserUpdateRequestDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.web.supports.UserSession;

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

    @GetMapping("/new")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult) throws NotValidUserInfoException {
        checkValidUser(bindingResult);
        userService.createNewUser(userDto);
        return "redirect:/login";
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
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
    public String editUserInfo(@UserSession LoginUser loginUser, @Valid UserUpdateRequestDto userUpdateRequestDto,
                               BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new NotValidUpdateUserInfoException(fieldError.getDefaultMessage());
        }
        userService.updateUser(loginUser.getEmail(), userUpdateRequestDto);
        loginUser.setUserName(userUpdateRequestDto.getUserName());
        httpSession.setAttribute("user", loginUser);
        return "redirect:/users/mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(@UserSession LoginUser loginUser, HttpSession httpSession) {
        userService.deleteUser(loginUser.getEmail());
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

    private void checkValidUser(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new NotValidUserInfoException(bindingResult.getFieldError().getDefaultMessage());
        }
    }

}
