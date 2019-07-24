package techcourse.myblog.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.application.dto.UserDto;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.domain.User;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("articles", articleService.findAll());
        log.info(String.valueOf(session.getAttribute("user")));

        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
        }
        return "index";
    }
}
