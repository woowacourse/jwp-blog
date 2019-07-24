package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserEditParams;
import techcourse.myblog.dto.UserSaveParams;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private static final String SUCCESS_SIGN_UP_MESSAGE = "회원 가입이 완료되었습니다!";
    private static final String ERROR_MISMATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다!";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public String signUp(UserSaveParams userSaveParams, Model model) {
        userService.save(userSaveParams.toEntity());
        model.addAttribute("successMessage", SUCCESS_SIGN_UP_MESSAGE);
        return "login";
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
    public String showMyPageEdit(String password, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "index";
        }

        if (!user.matchPassword(password)) {
            model.addAttribute("errorMessage", ERROR_MISMATCH_PASSWORD_MESSAGE);
            return "mypage-confirm";
        }

        return "mypage-edit";
    }

    @PutMapping("/mypage/edit")
    public String editMyPage(UserEditParams userEditParams, HttpSession httpSession) {
        User lastUser = (User) httpSession.getAttribute("user");
        Long id = lastUser.getId();
        User user = userService.update(id, userEditParams);
        httpSession.setAttribute("user", user);

        return "redirect:/mypage";
    }

    @DeleteMapping("/mypage")
    public String deleteUser(String email, HttpSession httpSession) {
        userService.deleteUser(email);
        httpSession.removeAttribute("user");

        return "redirect:/";
    }
}
