package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MypageController {
    private final UserService userService;

    public MypageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        setUserToModelBySession(model, session);
        return "mypage";
    }

    @PostMapping("/mypage")
    public RedirectView update(@ModelAttribute UserDto userDTO, HttpServletRequest request) {
        User user = userService.update(userDTO);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return new RedirectView("/mypage");
    }

    @GetMapping("/mypage/edit")
    public String updateForm(HttpSession session, Model model) {
        setUserToModelBySession(model, session);
        return "mypage-edit";
    }

    private void setUserToModelBySession(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
    }
}
