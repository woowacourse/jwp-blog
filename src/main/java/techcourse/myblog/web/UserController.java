package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserService;
import techcourse.myblog.domain.exception.DuplicatedUserException;
import techcourse.myblog.domain.exception.UserLoginException;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserInfo;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String createSignupForm(UserDto userDto, BindingResult bindingResult, Model model) {
        bindErrors(bindingResult, model);
        return "signup";
    }

    private void bindErrors(BindingResult bindingResult, Model model) {
        List<ObjectError> errors = (List<ObjectError>) model.asMap().get("errors");
        if (errors != null) {
            errors.forEach(error -> bindingResult.addError(error));
        }
    }

    @GetMapping("/users")
    public String userList(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @PostMapping("/users")
    public RedirectView save(@Validated({Default.class, UserInfo.class}) UserDto userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return redirectErrorView(bindingResult, redirectAttributes, "/signup");
        }

        try {
            userService.save(userDto.toUser());
        } catch (DuplicatedUserException e) {
            bindingResult.addError(new FieldError("userDto", e.getName(), e.getMessage()));
            return redirectErrorView(bindingResult, redirectAttributes, "/signup");
        }

        return new RedirectView("/login");
    }

    private RedirectView redirectErrorView(BindingResult bindingResult, RedirectAttributes redirectAttributes, String to) {
        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        return new RedirectView(to);
    }

    @DeleteMapping("/users")
    public RedirectView delete(UserDto userDto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user);
        return logout(session);
    }

    @GetMapping("/login")
    public String createLoginForm(UserDto userDto, BindingResult bindingResult, Model model) {
        bindErrors(bindingResult, model);
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(UserDto userDto,
                              BindingResult bindingResult,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(userDto);
            session.setAttribute("user", user);
            return new RedirectView("/");
        } catch (UserLoginException e) {
            bindingResult.addError(new FieldError("userDto", e.getName(), e.getMessage()));
            return redirectErrorView(bindingResult, redirectAttributes, "/login");
        }
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String createMyPageForm(UserDto userDto, BindingResult bindingResult, Model model) {
        bindErrors(bindingResult, model);
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView update(@Validated(UserInfo.class) UserDto userDto,
                               HttpSession session,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            return redirectErrorView(bindingResult, redirectAttributes, "/mypage/edit");
        }

        session.setAttribute("user", userService.update(user, userDto.getName()));
        return new RedirectView("/mypage");
    }
}
