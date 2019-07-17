package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.ArticleDto;
import techcourse.myblog.domain.ArticleService;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        List<ArticleDto> articleDtos = new ArticleAssembler().writeDtos(articleRepository.findAll());
        model.addAttribute("articles", articleDtos);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(ArticleDto article) {
        long articleId = articleService.createArticle(article);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        ArticleDto article = articleService.getArticleById(articleId);
        model.addAttribute("article", article);
        return "article";
    }
}
