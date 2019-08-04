package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.web.dto.UserAssembler;
import techcourse.myblog.web.dto.UserDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static techcourse.myblog.service.UserService.USER_SESSION_KEY;

@Controller
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "signup";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<UserDto> userDtos = userService.findAll().stream().map(UserAssembler::convertToDto)
            .collect(Collectors.toList());
        model.addAttribute("users", userDtos);
        return "user-list";
    }

    @PostMapping("/users")
    public String registerUsers(@Valid final UserRequestDto userRequestDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.save(userRequestDto);
        return "redirect:/login";
    }

    @DeleteMapping("/users")
    public String deleteUser(final HttpSession session) {
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        userService.delete(user.getId());
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        return "mypage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/mypage/mypage-edit")
    public String showMyPageEdit() {
        return "mypage-edit";
    }

    @PutMapping("/mypage/mypage-edit")
    public String editMyPage(final HttpSession session, final String name) {
        User user = (User) session.getAttribute(USER_SESSION_KEY);
        session.setAttribute(USER_SESSION_KEY, userService.update(user.getEmail(), name));
        return "redirect:/mypage";
    }
}
