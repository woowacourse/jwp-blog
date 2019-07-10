package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String createArticle(ArticleDto articleDto, Model model) {
        model.addAttribute("article", articleDto);
        Article article = new Article(
                articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents());
        articleRepository.insert(article);
        return "article";
    }

    @GetMapping("/")
    public String articles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String article(@PathVariable Integer articleId, Model model) {
        model.addAttribute("article", articleRepository.find(articleId));
        return "article";
    }
}
