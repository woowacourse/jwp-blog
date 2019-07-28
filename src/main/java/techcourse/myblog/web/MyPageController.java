package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private UserService userService;

    public MyPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String showMyPage(@PathVariable("id") long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", new UserProfileDto(user.getId(), user.getName(), user.getEmail()));
        return "mypage";
    }

    @GetMapping("/{id}/edit")
    public String showMyPageEdit(@PathVariable("id") long id, Model model, HttpSession httpSession) {
        UserController.validateUser(httpSession, id);
        User user = userService.findById(id);
        model.addAttribute("user", new UserProfileDto(user.getId(), user.getName(), user.getEmail()));
        return "mypage-edit";
    }
}
