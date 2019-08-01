package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;
import techcourse.myblog.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

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
        List<UserResponseDto> userResponseDtos = userService.findAll();
        model.addAttribute("users", userResponseDtos);
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

    @GetMapping("/mypage")
    public String showMyPage(final HttpSession session, Model model) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        model.addAttribute("user", user);
        return "mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(final HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        userService.delete(user);
        return "redirect:/logout";
    }

    @GetMapping("/logout")
    public String logOut(final HttpServletRequest request) {
        request.getSession().removeAttribute(USER_SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping("/mypage/mypage-edit")
    public String showMyPageEdit(final HttpSession session, Model model) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        model.addAttribute("user", user);
        return "mypage-edit";
    }

    @PutMapping("/mypage/mypage-edit")
    public String editMyPage(final HttpSession session, final String name) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        session.setAttribute(USER_SESSION_KEY, userService.update(user.getEmail(), name));
        return "redirect:/mypage";
    }
}
