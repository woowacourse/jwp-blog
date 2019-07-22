package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView write(String title, String coverUrl, String contents) {
        Article written = articleRepository.save(new Article(title, coverUrl, contents));
        return new RedirectView("/articles/" + written.getId());
    }

    @GetMapping("/articles/{articleId}")
    public String read(@PathVariable long articleId, Model model) {
        return articleRepository.findById(articleId).map(article -> {
                                                        model.addAttribute("article", article);
                                                        return "article";
                                                    }).orElse("redirect:/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable long articleId, Model model) {
        return articleRepository.findById(articleId).map(article -> {
                                                        model.addAttribute("article", article);
                                                        return "article-edit";
                                                    }).orElse("redirect:/");
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(@PathVariable long articleId, Article article) {
        return articleRepository.findById(articleId).map(ifExists -> {
            article.setId(articleId);
            articleRepository.save(article);
            return new RedirectView("/articles/" + articleId);
        }).orElse(new RedirectView("/"));
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId) {
        articleRepository.deleteById(articleId);
        return new RedirectView("/");
    }
}