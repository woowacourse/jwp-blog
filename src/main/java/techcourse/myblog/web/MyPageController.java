package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserPublicInfoDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {
    private UserService userService;

    public MyPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mypage/{id}")
    public String showMyPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.findUserPublicInfoById(id));
        return "mypage";
    }

    @GetMapping("/mypage/{id}/edit")
    public String showMyPageEdit(@PathVariable("id") long id, Model model,
                                 @LoggedUser UserPublicInfoDto userPublicInfoDto,
                                 RedirectAttributes redirectAttributes) {
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        if (userPublicInfoDto != null) {
            model.addAttribute("user", userService.findUserPublicInfoById(id));
            return "mypage-edit";
        }
        return "redirect:/mypage/" + id;
    }
}
