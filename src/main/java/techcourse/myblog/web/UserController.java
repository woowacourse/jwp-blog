package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.userinfo.UserPassword;
import techcourse.myblog.dto.UserEditRequestDto;
import techcourse.myblog.dto.UserSaveRequestDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private static final String SUCCESS_SIGN_UP_MESSAGE = "회원 가입이 완료되었습니다!";
    private static final String ERROR_MISMATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다!";
    private final UserService userService;

    @PostMapping("/users")
    public RedirectView signUp(UserSaveRequestDto userSaveRequestDto, RedirectAttributes redirectAttributes) {
        log.info("sign up post request params={}", userSaveRequestDto);

        userService.save(userSaveRequestDto.toEntity());
        redirectAttributes.addFlashAttribute("successMessage", SUCCESS_SIGN_UP_MESSAGE);

        return new RedirectView("/login");
    }

    @GetMapping("/users")
    public String fetchUsers(Model model) {
        Iterable<User> users = userService.findAll();

        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/mypage")
    public String showMyPage() {
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showMyPageConfirm() {
        return "mypage-confirm";
    }

    @PostMapping("/mypage/edit")
    public String showMyPageEdit(String password, Model model, User user) {
        if (mismatchPassword(password, user)) {
            model.addAttribute("errorMessage", ERROR_MISMATCH_PASSWORD_MESSAGE);
            return "mypage-confirm";
        }

        return "mypage-edit";
    }

    private boolean mismatchPassword(String password, User user) {
        return !user.matchPassword(new UserPassword(password));
    }

    @PutMapping("/mypage")
    public RedirectView editMyPage(UserEditRequestDto userEditRequestDto, User lastUser, HttpSession httpSession) {
        log.info("edit mypage put request params={}", userEditRequestDto);

        Long id = lastUser.getId();
        User user = userService.update(id, userEditRequestDto);
        httpSession.setAttribute(USER, user);

        return new RedirectView("/mypage");
    }

    @DeleteMapping("/mypage")
    public RedirectView deleteUser(User user, HttpSession httpSession) {
        Long id = user.getId();
        log.info("delete user delete request id={}", id);

        userService.deleteUser(id);
        httpSession.removeAttribute(USER);

        return new RedirectView("/");
    }
}
