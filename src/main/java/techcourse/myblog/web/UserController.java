package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User.User;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    private static final String ERROR_MESSAGE_NAME = "errorMessage";

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String findAll(final Model model) {
        final Iterable<User> users = userService.findAll();
        log.debug("users : {}", users);
        model.addAttribute("users", users);
        return "/user/user-list";
    }

    @PostMapping("/users")
    public String save(final UserRequestDto.SignUpRequestDto signUpRequestDto, final Model model) {
        if (userService.exitsByEmail(signUpRequestDto)) {
            model.addAttribute(ERROR_MESSAGE_NAME, "이메일이 중복됩니다");
            return "user/signup";
        }
        userService.save(signUpRequestDto);
        return "redirect:/login";
    }

    @GetMapping("/users/{id}")
    public String myPage(@PathVariable Long id, final Model model, final HttpSession session) {
        UserRequestDto.SessionDto sessionDto = (UserRequestDto.SessionDto) session.getAttribute(Constants.SESSION_USER_NAME);
        User user = userService.findById(id);
        log.debug("before authenticate...");

        if (authenticate(user, sessionDto)) {
            log.debug("authenticate...");
            log.debug("mypage/{} : User={}", id, user);
            log.debug("mypage/{} : Session={}", id, sessionDto);
            model.addAttribute("user", user);
            session.setAttribute(Constants.SESSION_USER_NAME, sessionDto);
            return "/mypage";
        }
        return "redirect:/users";
    }

    private boolean authenticate(User user, UserRequestDto.SessionDto sessionDto) {

        return (sessionDto != null) && (sessionDto.equals(UserRequestDto.SessionDto.toDto(user)));
    }

    @GetMapping("/users/edit/{id}")
    public String editPage(@PathVariable Long id, final Model model, final HttpSession session) {
        UserRequestDto.SessionDto sessionDto = (UserRequestDto.SessionDto) session.getAttribute(Constants.SESSION_USER_NAME);
        User user = userService.findById(id);

        if (authenticate(user, sessionDto)) {
            model.addAttribute("user", user);
            log.debug("{} to /mypage-edit", user);
            return "mypage-edit";
        }
        return "redirect:/users";
    }

    @PutMapping("/users/edit")
    public String update(final UserRequestDto.UpdateRequestDto updateRequestDto, final HttpSession session) {
        log.debug("updateRequestDto in update() : {}", updateRequestDto);
        User user = userService.update(updateRequestDto);
        session.setAttribute(Constants.SESSION_USER_NAME, UserRequestDto.SessionDto.toDto(user));
        return "redirect:/users/" + user.getId();
    }

    @DeleteMapping("/users/{email}")
    public String delete(@PathVariable String email, final HttpSession session) {
        userService.deleteByEmail(email);
        session.removeAttribute(Constants.SESSION_USER_NAME);
        return "redirect:/";
    }
}
