package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.SnsInfoRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserMypageRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;


@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

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
    public String showLoginPage(UserDto userDto, HttpSession session) {
        System.out.println(userDto);
        Optional<User> maybeUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            session.setAttribute("userId", user.getId());

            System.out.println(user);
            System.out.println(userDto);

            return "redirect:/";
        }

        System.out.println("로그인 망했다...");

        // 아... session 이 존재하면 무조건 세션을 보내는 건가??....
        session.invalidate();

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.debug("/logout handler called..!!");

        if (session.getAttribute("userId") != null) {
            session.removeAttribute("userId");
        }
        session.invalidate();

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
        Optional<UserResponseDto> user = userService.findById(id);

        if (user.isPresent()) {
            log.debug(user.get().toString());

            model.addAttribute("userInfo", user.get());

            return "mypage";
        }

        return "redirect:/";
    }

    @GetMapping("/users/{id}/mypage-edit")
    public String showMypageEdit(@PathVariable final long id, HttpSession session, Model model) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (userId == null || id != (long) userId) {
            return "redirect:/";
        }

        model.addAttribute("userInfo", userService.findById(id).get());

        return "mypage-edit";
    }

    @PutMapping("/users/{id}/mypage-edit")
    @Transactional
    public String updateUser(@PathVariable final long id, HttpSession session, UserMypageRequestDto dto) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (userId == null || id != (long) userId) {
            return "redirect:/";
        }

        log.debug(dto.toString());

        userService.update(id, dto);

        return "redirect:/users/" + id + "/mypage";
    }

    @DeleteMapping("/users/{id}/mypage-edit")
    public String deleteUser(@PathVariable final long id, HttpSession session) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (userId == null || id != (long) userId) {
            return "redirect:/";
        }

        log.debug(String.format("id: %d", id));

        userRepository.deleteById(id);
        userService.deleteById(id);

        return "redirect:/logout";
    }
}
