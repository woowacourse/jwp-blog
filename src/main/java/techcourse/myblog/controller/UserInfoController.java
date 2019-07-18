package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class UserInfoController {

    @GetMapping
    public String myPageForm() {
        return "mypage";
    }

    @GetMapping("/edit")
    public String myPageEditForm() {
        return "mypage-edit";
    }
}
