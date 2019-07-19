package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public String userLogin(UserLoginDto loginDto, HttpSession httpSession, Model model) {
        User user = userRepository.findUserByEmail(loginDto.getEmail());
        if (Objects.isNull(user)) {
            model.addAttribute(ERROR_MESSAGE, "이메일을 똑바로 입력하세요");
            return "redirect:/err";
        }
        if (!user.checkPassword(loginDto.getPassword())) {
            model.addAttribute(ERROR_MESSAGE, "패스워드가 올바르지 않습니다.");
            return "redirect:/err";
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
    public String signUp(UserDto userDto, Model model) {
        if (validateUser(userDto)) {
            User user = userDto.toEntity();
            userRepository.save(user);
            return "redirect:/login";
        }

        model.addAttribute(ERROR_MESSAGE, "잘못된 입력입니다.");
        return "redirect:/err";
    }

    @GetMapping("/mypage")
    public String showMyPage(HttpSession httpSession, Model model) {
        initMyPage(httpSession, model);
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String editMyPage(HttpSession httpSession, Model model) {
        try {
            initMyPage(httpSession, model);
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_MESSAGE, e);
            return "redirect:/err";
        }
        return "mypage-edit";
    }

    @PutMapping("/mypage/{id}")
    public String completeEditMypage(HttpSession httpSession, UserDto userDto, Model model) {
        String sessionEmail = httpSession.getAttribute(SESSION_EMAIL).toString();
        String email = userDto.getEmail();

        if (!sessionEmail.equals(email)) {
            model.addAttribute(ERROR_MESSAGE, "잘못된 접근입니다.");
            return "redirect:/err";
        }

        if (!(validateName(userDto.getName()) && validatePassword(userDto.getPassword()))) {
            model.addAttribute(ERROR_MESSAGE, "아이디와 비밀번호가 옳지 않습니다");
            return "redirect:/err";
        }

        User user = userRepository.findUserByEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());

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

    @DeleteMapping("/delete/user")
    public String deleteUser(HttpSession httpSession) {
        User user = userRepository.findUserByEmail(httpSession.getAttribute(SESSION_EMAIL).toString());
        userRepository.delete(user);
        httpSession.invalidate();
        return "redirect:/";
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
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        model.addAttribute("user", user);
    }
}
