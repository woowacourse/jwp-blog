package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.*;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepositoryImpl articleRepositoryImpl;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepositoryImpl;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/article/new")
    public String showWritingPage(Model model) {
        model.addAttribute("categories", categoryRepositoryImpl.findAll());
        return "article-edit";
    }

    @PostMapping("/articles/new")
    public String addArticle(Article articleParam) {
        //Article article = articleRepository.save(articleParam);
        //long latestId = article.getId();
        Long latestId = articleRepositoryImpl.add(articleParam);
        return "redirect:/articles/" + latestId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable long articleId, Model model) {
        Article article = articleRepositoryImpl.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, Article articleParam) {
        long updateId = articleRepositoryImpl.updateById(articleParam, articleId);
        return "redirect:/articles/" + updateId;
    }

    @DeleteMapping("articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepositoryImpl.deleteById(articleId);
        return "redirect:/";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepositoryImpl.findById(articleId);
        model.addAttribute("categories", categoryRepositoryImpl.findAll());
        model.addAttribute("article", article);
        return "article-edit";
    }
}
