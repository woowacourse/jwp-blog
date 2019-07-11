package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String writing(Model model) {
        model.addAttribute("method", "POST");
        return "article-edit";
    }

    @PostMapping("/articles")
    public String posting(Article article) {
        // TODO: 2019-07-10 아이디 처리...
        article.setId(articleRepository.getLastId() + 1);
        articleRepository.add(article);

        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        return "article";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.getArticles());
        return "index";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        model.addAttribute("method", "PUT");
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String putArticle(@PathVariable int id, Article article) {
        Article updatedArticle = articleRepository.getArticleById(id);
        updatedArticle.setTitle(article.getTitle());
        updatedArticle.setContents(article.getContents());
        updatedArticle.setCoverUrl(article.getCoverUrl());

        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.removeArticleById(id);
        return "redirect:/";
    }
}
