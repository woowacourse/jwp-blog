package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.dto.ArticleDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticlePage(HttpSession httpSession, ArticleDto articleDto) {
        log.debug(">>> save article : {}", articleDto);
        User user = (User)httpSession.getAttribute("user");

        Article article = articleRepository.save(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/")
    public String showArticlesPage(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticleByIdPage(HttpSession httpSession, ArticleDto articleDto) {
        log.debug(">>> put ArticleDto : {}", articleDto);
        User user = (User)httpSession.getAttribute("user");
        log.debug(">>> put Article before save: {}",articleDto.toArticle(user));
        Article article = articleRepository.save(articleDto.toArticle(user));
        log.debug(">>> put Article : {}", article);
        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId) {
        log.debug(">>> article Id : {}", articleId);
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
