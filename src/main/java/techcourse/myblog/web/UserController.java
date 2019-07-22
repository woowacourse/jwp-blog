package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
public class UserController {
    private static final String ERROR_MESSAGE = "message";
    private static final String SESSION_NAME = "name";
    private static final String SESSION_EMAIL = "signedEmail";
    private static final String NAME_PATTERN = "^([a-zA-Z]){2,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$";

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String showLogin(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(UserLoginDto loginDto, HttpSession httpSession) {
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        if (Objects.isNull(user)) {
            throw new UserException("아이디가 올바르지 않습니다.");
        }
        if (!user.checkPassword(loginDto.getPassword())) {
            throw new UserException("패스워드가 올바르지 않습니다.");
        }

        httpSession.setAttribute("SESSION",user);
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
    public String signUp(UserDto userDto) {
        if (validateUser(userDto)) {
            User user = userDto.toEntity();
            userRepository.save(user);
            return "redirect:/login";
        }
        throw new UserException("잘못된 입력입니다.");
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
    public String completeEditMypage(HttpSession httpSession, UserDto userDto) {
        String sessionEmail = httpSession.getAttribute(SESSION_EMAIL).toString();
        String email = userDto.getEmail();

        if (!sessionEmail.equals(email)) {
            throw new UserException("잘못된 접근입니다.");
        }

        if (!(validateName(userDto.getName()) && validatePassword(userDto.getPassword()))) {
            throw new UserException("아이디와 비밀번호가 올바르지 않습니다.");
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

    private boolean validateUser(UserDto userDto) {
        return validateName(userDto.getName())
                && validatePassword(userDto.getPassword())
                && validateEmail(userDto.getEmail());
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean validateEmail(String email) {
        return Objects.isNull(userRepository.findUserByEmail(email));
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
