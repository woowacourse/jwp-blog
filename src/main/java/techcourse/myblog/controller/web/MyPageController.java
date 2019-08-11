package techcourse.myblog.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.LoginUserDto;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private UserService userService;

    public MyPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String showMyPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.findUserPublicInfoById(id));
        return "mypage";
    }

    @GetMapping("/{id}/edit")
    public String showMyPageEdit(@PathVariable("id") long id, Model model,
                                 LoginUserDto user, RedirectAttributes redirectAttributes) {
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (user.matchId(id)) {
            model.addAttribute("user", userService.findUserPublicInfoById(id));
            return "mypage-edit";
        }
        return "redirect:/mypage/" + id;
    }
}
