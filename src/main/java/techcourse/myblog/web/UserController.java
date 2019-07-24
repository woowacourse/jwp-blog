package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserService;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.dto.UserInfo;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String createSignupForm(Model model) {
        bindingModelData(model);
        return "signup";
    }

    private void bindingModelData(Model model) {
        if (!model.containsAttribute("userDto")) {
            model.addAttribute("userDto", new UserDto("", "", ""));
        }
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
    public RedirectView save(@ModelAttribute("/signup") @Validated({Default.class, UserInfo.class}) UserDto userDto) {
        userService.save(userDto.toUser());
        return new RedirectView("/login");
    }

//    private RedirectView redirectErrorView(BindingResult bindingResult, RedirectAttributes redirectAttributes, String to) {
//        redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//        return new RedirectView(to);
//    }

    @DeleteMapping("/users")
    public RedirectView delete(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user);
        return logout(session);
    }

    @GetMapping("/login")
    public String createLoginForm(Model model) {
        bindingModelData(model);
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute("/login") UserDto userDto,
                              HttpSession session) {
        User user = userService.login(userDto.toUser());
        session.setAttribute("user", user);
        return new RedirectView("/");
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
    public String createMyPageForm(Model model) {
        bindingModelData(model);
        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView update(@ModelAttribute("/mypage/edit") @Validated(UserInfo.class) UserDto userDto,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", userService.update(user, userDto.getName()));
        return new RedirectView("/mypage");
    }
}
