package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.article.dto.ArticleResponse;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.user.dto.UserResponse;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

import java.util.List;
import java.util.Objects;

@Controller
public class MainController {
    private ArticleService articleService;

    @Autowired
    public MainController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model, final AccessUserInfo accessUserInfo) {
        List<ArticleResponse> articleDtos = articleService.findAll();
        model.addAttribute("articleDtos", articleDtos);
        UserResponse user = accessUserInfo.getUser();
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
