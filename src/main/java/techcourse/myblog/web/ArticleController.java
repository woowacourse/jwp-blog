package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("id", id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("background", article.getBackground());
        model.addAttribute("contents", article.getContents());
        return "article";
    }

    @GetMapping("/articles/new")
    private String getArticle(Model model) {
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

    @GetMapping("/articles/{id}/edit")
    private String getEditArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("id", id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("background", article.getBackground());
        model.addAttribute("contents", article.getContents());
        return "article-edit";
    }

    //    @RequestMapping(value = "/articles/{id}", method = RequestMethod.PUT)
    @PutMapping("/articles/{id}")
    private String putArticle(
            @RequestParam("title") String title,
            @RequestParam("contents") String contents,
            @RequestParam("coverUrl") String coverUrl,
            @PathVariable int id,
            Model model) {

        Article article = articleRepository.find(id);
        article.setTitle(title);
        article.setContents(contents);
        article.setBackground(coverUrl);

        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    private String deleteArticleById(@PathVariable int id) {
        articleRepository.delete(id);
        return "redirect:/";
    }

}
