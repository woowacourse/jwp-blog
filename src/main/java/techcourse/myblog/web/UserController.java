package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.UserInfo;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.DuplicatedEmailException;
import techcourse.myblog.service.EmailMissException;
import techcourse.myblog.service.PasswrodMissException;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String createSignupForm(Model model,
                                   UserDto userDto,
                                   BindingResult bindingResult) {
        checkErrors(model, bindingResult);
        return "signup";
    }

    private void checkErrors(Model model, BindingResult bindingResult) {
        List<ObjectError> errors = (List<ObjectError>) model.asMap().get("errors");
        if (errors != null) {
            errors.forEach(error -> bindingResult.addError(error));
        }
    }

    @PostMapping("/users")
    public RedirectView createUser(@ModelAttribute("userDto") @Validated({Default.class, UserInfo.class}) UserDto userDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return new RedirectView("/signup");
        }

        try {
            userService.save(userDto.toUser());
        } catch (DuplicatedEmailException e) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "email", e.getMessage())));
            return new RedirectView("/signup");
        }

        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String createLoginForm(Model model,
                                  UserDto userDto,
                                  BindingResult bindingResult) {
        checkErrors(model, bindingResult);

        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(HttpSession session,
                              @Validated(Default.class) UserDto userDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return new RedirectView("/login");
        }

        User loginUser = userDto.toUser();
        try {
            userService.login(loginUser);
        } catch (EmailMissException e) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "email", e.getMessage())));
            return new RedirectView("/login");
        } catch (PasswrodMissException e) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "password", e.getMessage())));
            return new RedirectView("/login");
        }

        session.setAttribute("user", loginUser);
        return new RedirectView("/login");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String createMyPageForm(Model model,
                                   UserDto userDto,
                                   BindingResult bindingResult) {
        checkErrors(model, bindingResult);
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView editUser(HttpSession session,
                                 @Validated(UserInfo.class) UserDto userDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return new RedirectView("/mypage/edit");
        }

        User user = (User) session.getAttribute("user");
        userDto.setEmail(user.getEmail());
        userService.modify(userDto.toUser());

        return new RedirectView("/mypage");
    }

    @DeleteMapping("/users")
    public RedirectView removeUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.remove(user);
        session.invalidate();

        return new RedirectView("/");
    }
}