package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import techcourse.myblog.controller.session.UserSessionManager;
import techcourse.myblog.repository.ArticleRepository;

@Controller
@AllArgsConstructor
public class IndexController {

    private final ArticleRepository articleRepository;
    private final UserSessionManager userSessionManager;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("user", userSessionManager.getUser());

        return "index";
    }
}
