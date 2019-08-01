package techcourse.myblog.presentation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.LoginService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserRequestDto;
import techcourse.myblog.service.dto.UserResponseDto;
import techcourse.myblog.web.SessionUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@Controller
public class UserController {
    public static final String EMAIL_DUPLICATION_ERROR_MSG = "이메일 중복입니다.";
    public static final String LOGIN_ERROR_MSG = "아이디나 비밀번호가 잘못되었습니다.";

    private final UserService userService;
    private final LoginService loginService;

    @GetMapping("/accounts/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("userRequestDto", new UserRequestDto());
        return "signup";
    }

    @PostMapping("/accounts/users")
    public String processSignup(@Valid UserRequestDto userRequestDto, Errors errors, HttpServletResponse response) {
        if (userService.canSave(userRequestDto)) {
            errors.rejectValue("email", "0", EMAIL_DUPLICATION_ERROR_MSG);
        }

        if (errors.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "signup";
        }

        userService.save(userRequestDto);
        return "redirect:/";
    }

    @GetMapping("/accounts/profile/{id}")
    public String showProfilePage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));

        return "mypage";
    }

    @GetMapping("/accounts/profile/edit")
    public String showProfileEditPage(Model model, @SessionUser User loggedInUser) {
        model.addAttribute("userRequestDto", new UserRequestDto(loggedInUser));

        return "mypage-edit";
    }

    @PutMapping("/accounts/profile/edit")
    public String processUpdateProfile(@Valid UserRequestDto userRequestDto, Errors errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "mypage-edit";
        }

        UserResponseDto dto = userService.update(userRequestDto.getEmail(), userRequestDto);
        loginService.login(request.getSession(), userRequestDto);

        return "redirect:/accounts/profile/" + dto.getId();
    }

    @GetMapping("/users")
    public String showUserList(Model model) {
        model.addAttribute("userList", userService.findAll());
        return "user-list";
    }

    @DeleteMapping("/accounts/delete")
    public RedirectView processDelete(HttpSession session, @SessionUser User loggedInUser) {
        userService.delete(loggedInUser);

        loginService.logout(session);

        return new RedirectView("/");
    }
}
