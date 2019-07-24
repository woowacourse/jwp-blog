package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequestMapping(value = "/users")
public class UserController {
    private static final String SESSION_USER = "user";
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login/page")
    public String showLogin(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(@Valid UserLoginDto loginDto, BindingResult bindingResult, HttpSession httpSession) {
        log.debug("Post Data : {}", loginDto);
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
        log.debug("Post Data : {}", userDto);
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
        log.debug("Put Data : {}", userDto);
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

    //    @GetMapping("/") 이렇게 하면 매핑이 안된다.
    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @DeleteMapping
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

    private Model initMyPage(HttpSession httpSession, Model model) {
        String email = getSessionEmail(httpSession);
        User user = userService.findUserByEmail(email);
        model.addAttribute("user", user);
        return model;
    }

    private String getSessionEmail(HttpSession httpSession) {
        return getUserSession(httpSession).getEmail();
    }

    private UserSession getUserSession(HttpSession httpSession) {
        return (UserSession) httpSession.getAttribute(SESSION_USER);
    }

    private void setUserSession(HttpSession httpSession, UserSession userSession) {
        httpSession.setAttribute(SESSION_USER, userSession);
    }
}
