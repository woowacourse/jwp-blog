package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String getNewArticlePage() {
        return "article-edit";
    }

    @PostMapping("/writing")
    public String writeNewArticle(@ModelAttribute("article") final Article article) {
        articleRepository.addArticle(article);
        return "redirect:/";
    }

    @Autowired
    void setArticleRepository(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
}
