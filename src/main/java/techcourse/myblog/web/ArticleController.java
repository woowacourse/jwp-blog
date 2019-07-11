package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    // 필드에서 애노테이션을 해주기보다는 생성자에서 해주는 것을 추천함
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/{articleId}")
    public String findArticleById(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId).ifPresent(a -> model.addAttribute("article", a));
        return "article";
    }
}
