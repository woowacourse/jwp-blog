package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.article.dto.ArticleResponse;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.user.dto.UserResponse;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

import java.util.Objects;

@Controller
public class MainController {
    private ArticleService articleService;

    @Autowired
    public MainController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(@PageableDefault(size = 2) Pageable pageable, Model model, final AccessUserInfo accessUserInfo) {
        Page<ArticleResponse> articles = articleService.findAll(pageable);
        model.addAttribute("articles", articles);
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
