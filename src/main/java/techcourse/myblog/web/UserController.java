package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
public class UserController {
    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    private static final String SESSION_NAME = "userInfo";

    private final UserRepository userRepository;

    @Autowired
    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(final HttpSession session) {
        if (session.getAttribute(SESSION_NAME) == null) {
            return "/user/login";
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(final UserDto.LoginInfo loginInfo, final HttpSession session, final Model model) {
        //TODO 메시지 보내는 방식으로 고치기
        if (canLogin(loginInfo.getEmail(), loginInfo.getPassword())) {
            // TODO 중복 get 제거
            User user = userRepository.findByEmail(loginInfo.getEmail()).orElseThrow(NoSuchElementException::new);
            UserDto.SessionUserInfo sessionUserInfo = UserDto.SessionUserInfo.toDto(user);
            session.setAttribute("userInfo", sessionUserInfo);
            return "/index";
        }
        model.addAttribute("errorMessage", "비밀번호를 올바르게 입력하시거나 회원가입을 해주세요");

        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout(final HttpSession session) {
        session.removeAttribute(SESSION_NAME);
        return "/index";
    }

    @GetMapping("/signup")
    public String signUp(final HttpSession session) {
        if (session.getAttribute(SESSION_NAME) == null) {
            return "/user/signup";
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String findUsers(final Model model) {
        final Iterable<User> users = userRepository.findAll();
        log.debug("users : {}", users);
        model.addAttribute("users", users);
        return "/user/user-list";
    }

    @PostMapping("/users")
    public String saveUser(final UserDto.SignUpUserInfo signUpUserInfo, final Model model) {
        //TODO 예외처리
        User user = signUpUserInfo.toUser();
        if (containsUser(user.getEmail())) {
            model.addAttribute("errorMessage", "이메일이 중복됩니다");
            return "/user/signup";
        }
        userRepository.save(user);
        return "redirect:/login";
    }

    //TODO NoSuchElementException -> CustomException으로 변화
    //TODO 세션값과 ID를 비교해서 다르면 user-list로 이동 에러메시지?
    @GetMapping("/users/{id}")
    public String myPage(@PathVariable Long id, final Model model, final HttpSession session) {
        UserDto.SessionUserInfo sessionUserInfo = (UserDto.SessionUserInfo) session.getAttribute(SESSION_NAME);

        log.debug("session value : {}", sessionUserInfo);
        log.debug("id : {}", id);

        if (sessionUserInfo != null && sessionUserInfo.getId() == id) {
            User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
            model.addAttribute("user", user);
            return "mypage";
        }
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editPage(@PathVariable Long id, final Model model) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/users/edit")
    public String update(final UserDto.updateInfo updateInfo, final HttpSession session) {
        User user = userRepository.findByEmail(updateInfo.getEmail()).orElseThrow(NoSuchElementException::new);
        user.setName(updateInfo.getName());
        userRepository.save(user);
        session.setAttribute(SESSION_NAME, UserDto.SessionUserInfo.toDto(user));

        return "redirect:/users/" + user.getId();
    }

    @DeleteMapping("/users/{email}")
    public String delete(@PathVariable String email, final HttpSession session) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        userRepository.delete(user);
        session.removeAttribute(SESSION_NAME);
        return "/index";
    }

    private boolean canLogin(final String email, final String password) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
            return user.matchPassword(password);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean containsUser(final String email) {
        return userRepository.existsByEmail(email);
    }
}
