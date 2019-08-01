package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.article.ArticleService;
import techcourse.myblog.service.dto.article.ArticleResponse;
import techcourse.myblog.service.dto.user.UserResponse;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@Controller
public class MainController {
    private ArticleService articleService;

    @Autowired
    public MainController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model, final HttpSession session) {
        List<ArticleResponse> articleDtos = articleService.findAll();
        model.addAttribute("articleDtos", articleDtos);
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (!Objects.isNull(user)) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }
}
