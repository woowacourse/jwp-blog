package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    private String getIndex(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }


    @GetMapping("/articles/{id}")
    private String getArticleById(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("contents", article.getContents());
        return "article";
    }

    @GetMapping("/articles/new")
    private String getArticle(Model model) {
        model.addAttribute("message", "articles");
        return "article-edit";
    }

    @GetMapping("/writing")
    private String getArticleEdit() {
        return "article-edit";
    }

    @PostMapping("/articles")
    private String postArticle(
            @RequestParam("title") String title,
            @RequestParam("contents") String contents,
            @RequestParam("coverUrl") String coverUrl,
            Model model) {
        Article article = new Article(title, contents, coverUrl);
        articleRepository.addBlog(article);
        int id = articleRepository.findAll().size() - 1;

        return "redirect:/articles/" + id;
    }
}
