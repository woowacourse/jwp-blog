package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyPageController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    @GetMapping("/mypage")
    public String showMyPage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) == null) {
            return "redirect:/";
        }
        return "mypage";
    }

    @GetMapping("/mypage/edit")
    public String showMyPageEdit(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) == null) {
            return "redirect:/";
        }
        return "mypage-edit";
    }
}
