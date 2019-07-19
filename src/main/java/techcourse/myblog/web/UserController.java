package techcourse.myblog.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String showLogin(HttpSession httpSession) {
        if (checkLogedIn(httpSession)) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String userLogin(User user, HttpSession httpSession, Model model) {
        User dbUser = userRepository.findUserByEmail(user.getEmail());
        if (Objects.isNull(dbUser)) {
            model.addAttribute("message", "이메일을 똑바로 입력하세요");
            return "redirect:/err";
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            model.addAttribute("message", "패스워드가 올바르지 않습니다.");
            return "redirect:/err";
        }

        httpSession.setAttribute("name", dbUser.getName());
        httpSession.setAttribute("signedEmail", dbUser.getEmail());
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
    public String signUp(User user, Model model) {
        if (validateUser(user)) {
            userRepository.save(user);
            return "redirect:/login";
        }

        model.addAttribute("message", "잘못된 입력입니다.");
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
            model.addAttribute("message", e);
            return "redirect:/err";
        }
        return "mypage-edit";
    }

    @PutMapping("/mypage/{id}")
    public String completeEditMypage(HttpSession httpSession, Model model, User user, RedirectAttributes redirectAttributes) {
        User oldUser = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);
        oldUser.setName(user.getName());
        oldUser.setPassword(user.getPassword());

        String signedEmail = httpSession.getAttribute("signedEmail").toString();
        String email = oldUser.getEmail();

        if (!signedEmail.equals(email)) {
            redirectAttributes.addFlashAttribute("message", "잘못된 접근입니다.");
            return "redirect:/err";
        }

        if (!(validateName(user.getName()) && validatePassword(user.getPassword()))) {
            redirectAttributes.addFlashAttribute("message", "아이디와 비밀번호가 옳지 않습니다");
            return "redirect:/err";
        }

        userRepository.save(oldUser);
        httpSession.setAttribute("name", oldUser.getName());

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
        User user = userRepository.findUserByEmail(httpSession.getAttribute("signedEmail").toString());
        userRepository.delete(user);
        httpSession.invalidate();
        return "redirect:/";
    }

    private boolean validateUser(User user) {
        return validateName(user.getName())
                && validatePassword(user.getPassword())
                && validateEmail(user.getEmail());
    }

    private boolean validateName(String name) {
        Pattern pattern = Pattern.compile("^([a-zA-Z]){2,10}$");
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,20}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean validateEmail(String email) {
        return Objects.isNull(userRepository.findUserByEmail(email));
    }


    private boolean checkLogedIn(HttpSession httpSession) {
        return !Objects.isNull(httpSession.getAttribute("signedEmail"));
    }

    private void initMyPage(HttpSession httpSession, Model model) {
        String email = httpSession.getAttribute("signedEmail").toString();

        User user = userRepository.findUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }
        model.addAttribute("user", user);
    }
}
