package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserMypageRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.exception.UserNotFoundException;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;


@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/signup")
    public String showSignUpPage() {
        // TODO: 로그인 된 유저인지 체크
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        // TODO: 로그인 된 유저인지 체크
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(UserDto userDto, HttpSession session) {
        log.debug("userDto: {}", userDto);

        User user = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword())
                .orElseThrow(() -> new UserNotFoundException());

        loginService.login(session, user.getEmail(), user.getPassword());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("called..!!");

        loginService.logout(session);

        return "redirect:/";
    }

    @PostMapping("/users")
    public String add(UserSaveRequestDto userDto) {
        log.info(userDto.toString());
        userService.save(userDto);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUserListPage(Model model) {
        log.debug("called..!!");

        model.addAttribute("users", userService.findAll());

        return "user-list";
    }

    @GetMapping("users/{id}/mypage")
    public String showMypage(@PathVariable final long id, Model model) {
        UserResponseDto userDto = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        model.addAttribute("userInfo", userDto);

        return "mypage";
    }

    @GetMapping("/users/{id}/mypage-edit")
    public String showMypageEdit(@PathVariable final long id, HttpSession session, Model model) {
        if (!loginService.isLoggedInUser(session, id)){
            return "redirect:/";
        }

        model.addAttribute("userInfo", userService.findById(id).get());

        return "mypage-edit";
    }

    @PutMapping("/users/{id}/mypage-edit")
    @Transactional
    public String updateUser(@PathVariable final long id, HttpSession session, UserMypageRequestDto dto) {
        if (!loginService.isLoggedInUser(session, id)){
            return "redirect:/";
        }

        log.debug(dto.toString());

        userService.update(id, dto);

        return "redirect:/users/" + id + "/mypage";
    }

    @DeleteMapping("/users/{id}/mypage-edit")
    public String deleteUser(@PathVariable final long id, HttpSession session) {
        if (!loginService.isLoggedInUser(session, id)){
            return "redirect:/";
        }

        log.debug(String.format("id: %d", id));

        userService.deleteById(id);
        loginService.logout(session);

        return "redirect:/logout";
    }
}
