package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class UserController {
    private static final String SESSION_USER = "user";

    private final UserService userService;

    @GetMapping("/login/page")
    public String showLogin(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@Valid UserLoginDto loginDto, BindingResult bindingResult, HttpSession httpSession) {
        checkBindingError(bindingResult);
        User user = userService.login(loginDto);
        UserSession userSession = new UserSession(user);
        setUserSession(httpSession, userSession);
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
        userService.save(userDto);
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

        String sessionEmail = getUserSession(httpSession).getEmail();
        String email = userDto.getEmail();
        if (!sessionEmail.equals(email)) {
            throw new UserException("잘못된 접근입니다.");
        }

        User user = userService.update(userDto);
        UserSession userSession = new UserSession(user);
        setUserSession(httpSession, userSession);
        return "redirect:/mypage";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @DeleteMapping("/user")
    public String deleteUser(HttpSession httpSession) {
        userService.delete(getUserSession(httpSession).getEmail());
        httpSession.invalidate();
        return "redirect:/";
    }

    private void checkBindingError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            throw new UserException(fieldError.getDefaultMessage());
        }
    }

    private boolean checkLogedIn(HttpSession httpSession) {
        return !Objects.isNull(httpSession.getAttribute(SESSION_USER));
    }

    private void initMyPage(HttpSession httpSession, Model model) {
        UserSession userSession = getUserSession(httpSession);
        String email = userSession.getEmail();

        User user = userService.findUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UserException("잘못된 접근입니다.");
        }
        model.addAttribute("user", user);
    }

    private UserSession getUserSession(HttpSession httpSession) {
        return (UserSession) httpSession.getAttribute(SESSION_USER);
    }

    private void setUserSession(HttpSession httpSession, UserSession userSession) {
        httpSession.setAttribute(SESSION_USER, userSession);
    }
}
