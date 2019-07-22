package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.controller.dto.UserDTO;
import techcourse.myblog.model.User;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MypageController {
    private UserService userService;

    @Autowired
    public MypageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mypage")
    public String myPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/mypage")
    public String updateProfile(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        User user = userService.update(userDTO);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/edit")
    public String myPageEdit(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "mypage-edit";
    }
}
