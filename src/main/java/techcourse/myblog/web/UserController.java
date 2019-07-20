package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    public static final String DUPLICATED_USER_MESSAGE = "이미 존재하는 email입니다";
    public static final String WRONG_EMAIL_MESSAGE = "이메일을 확인해주세요";
    public static final String WRONG_PASSWORD_MESSAGE = "비밀번호를 확인해주세요";
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/signup")
    public String createSignupForm(Model model,
                                   UserDto userDto,
                                   BindingResult bindingResult) {
        List<ObjectError> errors = (List<ObjectError>) model.asMap().get("errors");
        if (errors != null) {
            errors.forEach(error -> bindingResult.addError(error));
        }
        return "signup";
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
            userRepository.save(userDto.toUser());
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "email", DUPLICATED_USER_MESSAGE)));
            return new RedirectView("/signup");
        }

        return new RedirectView("/login");
    }

    @GetMapping("/login")
    public String createLoginForm(Model model,
                                  UserDto userDto,
                                  BindingResult bindingResult) {
        List<ObjectError> errors = (List<ObjectError>) model.asMap().get("errors");
        if (errors != null) {
            errors.forEach(error -> bindingResult.addError(error));
        }

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

        Optional<User> loginUser = userRepository.findByEmail(userDto.getEmail());

        if (!loginUser.isPresent()) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "email", WRONG_EMAIL_MESSAGE)));
            return new RedirectView("/login");
        }

        if (!loginUser.get().matchPassword(userDto.toUser())) {
            redirectAttributes.addFlashAttribute("errors", Arrays.asList(
                    new FieldError("userDto", "password", WRONG_PASSWORD_MESSAGE)));
            return new RedirectView("/login");
        }

        session.setAttribute("user", loginUser.get());
        return new RedirectView("/login");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
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

        List<ObjectError> errors = (List<ObjectError>) model.asMap().get("errors");
        if (errors != null) {
            errors.forEach(error -> bindingResult.addError(error));
        }

        return "mypage-edit";
    }

    @PutMapping("/mypage")
    public RedirectView editUser(HttpSession session,
                                 @Validated(UserInfo.class) UserDto userDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return new RedirectView("/mypage/edit");
        }

        user.modifyName(userDto.getName());
        userRepository.save(user);
        return new RedirectView("/mypage");
    }

    @DeleteMapping("/users")
    public RedirectView removeUser(UserDto userDto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.matchEmail(userDto.toUser())) {
            userRepository.delete(user);
        }
        session.invalidate();
        return new RedirectView("/");
    }
}