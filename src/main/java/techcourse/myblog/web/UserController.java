package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.Optional;


@Controller
public class UserController {
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
        Optional<UserDto> maybeUserDto = userService.findUser(userDto);

        if (maybeUserDto.isPresent()) {
            UserDto toUserDto = maybeUserDto.get();
            session.setAttribute("userId", toUserDto.getId());

            return "redirect:/";
        }
        return "";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session.getAttribute("userId") != null) {
            session.removeAttribute("userId");
        }
        return "redirect:/";
    }

    @PostMapping("/users")
    public String addUser(UserDto userDto) {
        userService.create(userDto);

        return "redirect:/login";
    }

    @GetMapping("/users")
    public String showUserListPage(Model model) {
        model.addAttribute("users", userService.readAll());

        return "user-list";
    }

    @GetMapping("users/{id}/mypage")
    public String showMypage(@PathVariable final long id, Model model) {
        Optional<UserDto> maybeUserDto = userService.readWithoutPasswordById(id);

        if (maybeUserDto.isPresent()) {
            model.addAttribute("userInfo", maybeUserDto.get());

            return "mypage";
        }

        return "redirect:/";
    }

    @GetMapping("/users/{id}/mypage-edit")
    public String showMypageEdit(@PathVariable final long id, HttpSession session, Model model) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (id != (long) userId) {
            return "redirect:/";
        }

        Optional<UserDto> maybeUserDto = userService.readWithoutPasswordById(id);

        if (maybeUserDto.isPresent()) {
            model.addAttribute("userInfo", maybeUserDto.get());
            return "mypage-edit";
        }
        return "error";
    }

    @PutMapping("/users/{id}/mypage-edit")
    @Transactional
    public String updateUser(@PathVariable final long id, HttpSession session, UserDto userDto) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (id != (long) userId) {
            return "redirect:/";
        }
        UserDto updateUserDto = userService.updateUser(id, userDto);

        return "redirect:/users/" + updateUserDto.getId() + "/mypage";
    }

    @DeleteMapping("/users/{id}/mypage-edit")
    public String deleteUser(@PathVariable final long id, HttpSession session) {
        // TODO: 로그인되었는지 확인 (아니면 메인으로)

        Object userId = session.getAttribute("userId");
        if (userId == null || id != (long) userId) {
            return "redirect:/";
        }

        userService.deleteById(id);

        return "redirect:/logout";
    }
}
