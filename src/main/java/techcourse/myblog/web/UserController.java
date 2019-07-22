package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class UserController {
    private static final String ERROR_MESSAGE = "message";
    private static final String SESSION_NAME = "name";
    private static final String SESSION_EMAIL = "signedEmail";

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String showLogin(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@Valid UserLoginDto loginDto, BindingResult bindingResult, HttpSession httpSession) {
        checkBindingError(bindingResult);

        User user = userRepository.findUserByEmail(loginDto.getEmail());
        if (Objects.isNull(user)) {
            throw new UserException("아이디가 올바르지 않습니다.");
        }

        httpSession.setAttribute(SESSION_NAME, user.getName());
        httpSession.setAttribute(SESSION_EMAIL, user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/join")
    public String showSignUp(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/join")
    public String signUp(@Valid UserDto userDto, BindingResult bindingResult) {
        checkBindingError(bindingResult);

        User user = userDto.toEntity();
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession, Model model) {
        initMyPage(httpSession, model);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String editMyPage(HttpSession httpSession, Model model) {
        initMyPage(httpSession, model);
        return "mypage-edit";
    }

    @PutMapping("/mypage/save")
    public String completeEditMypage(HttpSession httpSession, @Valid UserDto userDto, BindingResult bindingResult) {
        checkBindingError(bindingResult);
        String sessionEmail = httpSession.getAttribute(SESSION_EMAIL).toString();
        String email = userDto.getEmail();

        if (!sessionEmail.equals(email)) {
            throw new UserException("잘못된 접근입니다.");
        }
        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.update(userDto);
        userRepository.save(user);
        httpSession.setAttribute(SESSION_NAME, user.getName());

        return "redirect:/mypage";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @DeleteMapping("/user")
    public String deleteUser(HttpSession httpSession) {
        User user = userRepository.findUserByEmail(httpSession.getAttribute(SESSION_EMAIL).toString());
        userRepository.delete(user);
        httpSession.invalidate();
        return "redirect:/";
    }

    @ExceptionHandler
    public String handleUserException(UserException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, e.getMessage());
        return "redirect:/err";
    }

    private void checkBindingError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new UserException(fieldError.getDefaultMessage());
        }
    }

    private boolean checkLogedIn(HttpSession httpSession) {
        return !Objects.isNull(httpSession.getAttribute(SESSION_EMAIL));
    }

    private void initMyPage(HttpSession httpSession, Model model) {
        String email = httpSession.getAttribute(SESSION_EMAIL).toString();

        User user = userRepository.findUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UserException("잘못된 접근입니다.");
        }
        model.addAttribute("user", user);
    }
}
