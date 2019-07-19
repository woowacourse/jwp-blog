package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.domain.UserUpdateRequestDto;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.exception.NotValidUpdateUserInformation;
import techcourse.myblog.repo.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/new")
    public String createUser(@Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            model.addAttribute("error", fieldError.getDefaultMessage());
            return "signup";
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            model.addAttribute("error", "중복된 이메일 입니다.");
            return "signup";
        }

        User user = new User(userDto);
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping
    public String findAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showEditPage(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "mypage-edit";
    }

    @Transactional
    @PutMapping("/mypage/edit")
    public String editUserInfo(@Valid UserUpdateRequestDto userUpdateRequestDto,
                               BindingResult bindingResult, HttpSession httpSession) throws NotValidUpdateUserInformation {
        if (bindingResult.hasErrors()) {
            throw new NotValidUpdateUserInformation(bindingResult.getFieldError());
        }
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        user.setUserName(userUpdateRequestDto.getUserName());
        httpSession.setAttribute("user", user);
        return "redirect:/users/mypage";
    }

    @Transactional
    @DeleteMapping("/mypage")
    public String deleteUser(HttpSession httpSession) {
        String email = ((User) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundObjectException::new);
        httpSession.removeAttribute("user");
        userRepository.delete(user);
        return "redirect:/";
    }

    @ExceptionHandler(NotValidUpdateUserInformation.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNotValidUpdateInformation(FieldError fieldError, Model model) {
        log.error(fieldError.getDefaultMessage());
        model.addAttribute("error", fieldError.getDefaultMessage());
        return "mypage-edit";
    }

}
