package techcourse.myblog.web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.service.exception.AuthException;
import techcourse.myblog.web.UserSession;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.web.UserSession.USER_SESSION;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping("/new")
    public String signupForm() {
        return "signup";
    }

    @PostMapping
    @ResponseBody
    public Long signup(@Valid UserDto.Register userDto) {
        log.debug(userDto.toString());

        return userService.save(userDto);
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(UserDto.Register userDto, HttpSession session) {
        log.debug(userDto.toString());

        UserSession userSession = UserSession.createByUser(userService.login(userDto));

        session.setAttribute(USER_SESSION, userSession);
        return userSession.getName();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("logout : {}", session.getAttribute(USER_SESSION));
        session.removeAttribute(USER_SESSION);
        return "redirect:/";
    }

    @GetMapping
    public String userList(Model model) {
        List<UserDto.Response> users = userService.findAllExceptPassword();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id,
                       Model model,
                       UserSession userSession) {
        identify(userSession, id);

        UserDto.Response userResponseDto = userService.getUserById(id);
        model.addAttribute("user", userResponseDto);
        return "mypage";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           UserSession userSession) {
        identify(userSession, id);

        UserDto.Response userResponseDto = userService.getUserById(id);
        model.addAttribute("user", userResponseDto);
        return "mypage-edit";
    }

    @PutMapping("/{id}")
    public String edit(@Valid UserDto.Update userDto, UserSession userSession) {
        identify(userSession, userDto.getId());

        UserDto.Response responseDto = userService.update(userDto);
        return "redirect:/users/" + responseDto.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session, UserSession userSession) {
        identify(userSession, id);

        userService.deleteById(id);
        session.removeAttribute(USER_SESSION);
        return "redirect:/";
    }

    private void identify(UserSession userSession, Long id) {
        if (!id.equals(userSession.getId())) {
            throw new AuthException("본인이 아닙니다.");
        }
    }
}
